package owner.buriedoiltank.web;

import jakarta.validation.Valid;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.LeadCaptureRequest;
import owner.buriedoiltank.leads.LeadEventRequest;
import owner.buriedoiltank.leads.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LeadApiController {
    private final LeadService leadService;
    private final EventLogService eventLogService;

    public LeadApiController(LeadService leadService, EventLogService eventLogService) {
        this.leadService = leadService;
        this.eventLogService = eventLogService;
    }

    @PostMapping(path = "/api/leads/capture", consumes = "application/x-www-form-urlencoded")
    public String captureLead(@Valid @ModelAttribute LeadCaptureRequest request, BindingResult bindingResult) {
        String returnPath = sanitizeReturnPath(request.getPagePath());
        if (bindingResult.hasErrors()) {
            return "redirect:" + returnPath + "?lead=error";
        }
        try {
            leadService.captureLead(request);
        } catch (IllegalArgumentException exception) {
            return "redirect:" + returnPath + "?lead=error";
        }
        return "redirect:" + returnPath + "?lead=success";
    }

    @PostMapping(path = "/api/leads/event", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public ResponseEntity<Void> recordEvent(@Valid @ModelAttribute LeadEventRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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
        if (path == null || path.isBlank() || !path.startsWith("/")) {
            return "/";
        }
        if (path.startsWith("//")) {
            return "/";
        }
        return path;
    }
}
