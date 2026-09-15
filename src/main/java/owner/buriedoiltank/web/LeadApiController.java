package owner.buriedoiltank.web;

import jakarta.validation.Valid;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.CaseActivityService;
import owner.buriedoiltank.leads.CaseActivityType;
import owner.buriedoiltank.leads.CustomerReceiptService;
import owner.buriedoiltank.leads.DocumentStorageService;
import owner.buriedoiltank.leads.LeadCaptureRequest;
import owner.buriedoiltank.leads.LeadEventRequest;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.leads.RecordResearchNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class LeadApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeadApiController.class);
    private final LeadService leadService;
    private final EventLogService eventLogService;
    private final ApiRequestProtectionService apiRequestProtectionService;
    private final RecordResearchNotificationService notificationService;
    private final DocumentStorageService documentStorageService;
    private final CaseActivityService caseActivityService;
    private final CustomerReceiptService customerReceiptService;

    public LeadApiController(
            LeadService leadService,
            EventLogService eventLogService,
            ApiRequestProtectionService apiRequestProtectionService,
            RecordResearchNotificationService notificationService,
            DocumentStorageService documentStorageService,
            CaseActivityService caseActivityService,
            CustomerReceiptService customerReceiptService
    ) {
        this.leadService = leadService;
        this.eventLogService = eventLogService;
        this.apiRequestProtectionService = apiRequestProtectionService;
        this.notificationService = notificationService;
        this.documentStorageService = documentStorageService;
        this.caseActivityService = caseActivityService;
        this.customerReceiptService = customerReceiptService;
    }

    @PostMapping(path = "/api/leads/capture")
    public String captureLead(
            @Valid @ModelAttribute LeadCaptureRequest request,
            BindingResult bindingResult,
            @RequestParam(name = "documents", required = false) List<MultipartFile> documents,
            HttpServletRequest httpServletRequest
    ) {
        String returnPath = sanitizeReturnPath(request.getPagePath());
        if (!apiRequestProtectionService.isTrustedRequest(httpServletRequest)) {
            LOGGER.warn("Rejected lead capture because neither Origin nor Referer matched the configured site origin");
            return "redirect:" + returnPath + "?lead=error";
        }
        if (!apiRequestProtectionService.tryConsumeLeadCapture(httpServletRequest)) {
            return "redirect:" + returnPath + "?lead=busy";
        }
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Rejected lead capture with invalid fields: {}",
                    bindingResult.getFieldErrors().stream().map(error -> error.getField()).distinct().toList());
            return "redirect:" + returnPath + "?lead=error";
        }
        if ("record-research".equals(request.getRouteFamily())
                && (isBlank(request.getPropertyAddress())
                || isBlank(request.getCountyMunicipality())
                || isBlank(request.getPrimaryQuestion())
                || isBlank(request.getHasDocuments()))) {
            LOGGER.warn("Rejected record-research lead capture because one or more required case fields were blank");
            return "redirect:" + returnPath + "?lead=error";
        }
        if ("record-research".equals(request.getRouteFamily())) {
            request.setPartnerType("record_research");
        }
        String receiptCode = "";
        try {
            documentStorageService.validate(documents);
            LeadService.CapturedLead capturedLead = leadService.captureLead(request);
            int storedDocuments = documentStorageService.store(capturedLead.leadId(), documents);
            if ("record-research".equals(request.getRouteFamily())) {
                String submissionSource = isBlank(request.getSubmissionToken())
                        ? "lead:" + capturedLead.leadId()
                        : "submission:" + request.getSubmissionToken().trim();
                caseActivityService.recordIfAbsent(
                        capturedLead.leadId(), CaseActivityType.INTAKE.slug(), submissionSource,
                        "", "", "web-form", "saved", "Property research intake saved",
                        "Begin parcel and source review", ""
                );
                int documentCount = documentStorageService.forLead(capturedLead.leadId()).size();
                if (documentCount > 0) {
                    caseActivityService.recordIfAbsent(
                            capturedLead.leadId(), CaseActivityType.DOCUMENT_RECEIVED.slug(),
                            "document-register:" + documentCount, "", "", "secure-upload",
                            (storedDocuments > 0 ? storedDocuments : documentCount) + " file(s) stored",
                            "Customer documents received with intake",
                            "Review documents against the source record", ""
                    );
                }
                var savedCase = leadService.requireLead(capturedLead.leadId());
                try {
                    notificationService.queueIfNeeded(savedCase);
                } catch (RuntimeException exception) {
                    LOGGER.warn("The saved case could not enter the operator notification queue; review it in admin");
                }
                try {
                    customerReceiptService.queueIfNeeded(savedCase);
                } catch (RuntimeException exception) {
                    LOGGER.warn("The saved case could not enter the customer receipt queue; review it in admin");
                }
                receiptCode = CustomerReceiptService.receiptCode(capturedLead.leadId());
            }
        } catch (IllegalArgumentException exception) {
            LOGGER.warn("Rejected lead capture because a routing value was invalid");
            return "redirect:" + returnPath + "?lead=error";
        } catch (IllegalStateException exception) {
            LOGGER.error("A record-research intake could not be stored completely", exception);
            return "redirect:" + returnPath + "?lead=error";
        }
        return "redirect:" + returnPath + "?lead=success"
                + (receiptCode.isBlank() ? "" : "&receipt=" + receiptCode);
    }

    @PostMapping(path = "/api/leads/event", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public ResponseEntity<Void> recordEvent(
            @Valid @ModelAttribute LeadEventRequest request,
            BindingResult bindingResult,
            HttpServletRequest httpServletRequest
    ) {
        if (!apiRequestProtectionService.isTrustedRequest(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!apiRequestProtectionService.tryConsumeEvent(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        if (!eventLogService.isClientEventType(request.getEventType())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            eventLogService.recordEvent(request);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private static String sanitizeReturnPath(String path) {
        if (path == null || path.isBlank()
                || !path.matches("^/[A-Za-z0-9/_-]*$")
                || path.startsWith("//")
                || path.contains("//")
                || path.contains("/../")
                || path.endsWith("/..")) {
            return "/";
        }
        return path;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
