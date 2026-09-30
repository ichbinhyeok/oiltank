package owner.buriedoiltank.data;

import java.util.List;

/** Editorial comparison aids, not a document validator or an agency conclusion. */
public final class RecordEvidence {
    private RecordEvidence() {}
    public record Check(String document, String compare, String gap) {}
    public record Guide(String target, String firstRequest, List<Check> checks) {}
    private static Check c(String document, String compare, String gap) { return new Check(document, compare, gap); }
    private static Guide g(String target, String request, Check... checks) { return new Guide(target, request, List.of(checks)); }

    public static Guide forEntry(ResearchCatalog.Entry entry) {
        return switch (entry.slug()) {
            case "njdep-nfa-letter" -> g("A copy of the issued NFA tied to the environmental case—not only a closed status.",
                "the issued UHOT No Further Action letter and its referenced closure documents",
                c("Issued NFA letter", "Compare the incident/PI reference, property description, issue date and stated scope.", "If you only have a database status, the letter itself is still missing."),
                c("Referenced reports", "List the reports and attachments named in the letter separately from the files you possess.", "Request a named missing report; do not describe all remediation paperwork as delivered."),
                c("Local completion file", "Keep municipal permit and inspection documents in a separate line.", "An environmental letter does not replace the local work record."));
            case "njdep-tank-documents" -> g("An identifiable report or attachment associated with the correct NJDEP case.",
                "the case report, attachments and final correspondence associated with the known incident or PI number",
                c("Case reference", "Compare the address and PI/incident number across DataMiner and the document.", "A report title or index entry alone is not the report."),
                c("Document and attachments", "Record title, author/issuer, date, revision and included attachments.", "Name missing appendices or final correspondence in the request."));
            case "nysdec-spill-documents" -> g("The report and relevant correspondence behind a DEC spill reference.",
                "the spill investigation report, referenced attachments and closure correspondence",
                c("Spill listing", "Compare spill number, location and incident date before associating documents.", "An incident listing is a search reference, not a complete report."),
                c("Underlying file", "Separate the investigation report, attachments and closure correspondence actually received.", "If only the listing is available, request the file by spill number."));
            case "oregon-clean-decommissioning" -> g("The clean-decommissioning record and available supporting document for the matching site.",
                "the HOT clean-decommissioning certificate and available supporting decommissioning report",
                c("Certificate record", "Compare the PLC reference, location and work described with your paperwork.", "A search result or contractor license is not the site's decommissioning certificate."),
                c("Available document", "Open the document control and keep the actual file, its title and date.", "If the document control has no file, record an attachment gap and use DEQ records access."),
                c("Older or leak-related material", "Keep older work dates and any separate leaking-HOT reference visible.", "Use the cleanup route for a leak file; do not substitute a clean-certificate search."));
            case "oregon-leaking-tank-records" -> g("A matching leaking-HOT project with the cleanup reports and correspondence you actually obtained.",
                "the leaking heating oil tank cleanup report, attachments and final correspondence",
                c("Project information", "Compare the DEQ LUST/project number, address and dates.", "Do not treat another project at a similar address as a match."),
                c("Project documents", "Separate the cleanup report from final correspondence and laboratory attachments.", "A project status alone does not show which documents you hold."));
            case "seattle-residential-tank-records" -> g("A permit reference plus the follow-up decommissioning information—not just an issued permit.",
                "the residential oil tank decommissioning permit and associated follow-up report",
                c("Permit record", "Compare address, permit number and issue date in the residential dataset.", "An issued permit does not establish completed tank work."),
                c("Follow-up information", "Check the decommissioning date and work type, including removal versus abandonment.", "Blank follow-up fields remain an unresolved report gap; request the underlying record."));
            case "maine-tank-spill-records" -> g("The relevant registration record or finalized spill document, with the record family clearly identified.",
                "the identified tank registration record and any separately referenced finalized spill report",
                c("Registration", "Compare facility/tank reference and town/address.", "Registration information is not a spill cleanup report."),
                c("Spill document", "Compare spill reference, report date and location with the registration if applicable.", "A missing online file may require file-room review; keep the request pending until a response arrives."));
            case "connecticut-deep-spill-documents" -> g("The DEEP report for the identified case—not only an incident dataset row.",
                "the spill report and identified attachments for the known DEEP agency or case ID",
                c("Portal result", "Compare Town, Street Address, Agency ID, program and document type.", "A result from another program or nearby property is not the requested file."),
                c("Delivered document", "Check the report date and referenced attachments against what downloaded.", "Use the Records Center/FOIA route for a specified missing document, not another broad search."));
            case "nassau-ny" -> g("The existing county removal or abandonment verification letter for the matching address.",
                "the existing small heating-oil tank removal or abandonment verification letter",
                c("Verification letter", "Compare address, work date and removal versus abandonment wording.", "Scheduling new work is not retrieval of a historic verification letter."),
                c("Separate work files", "List any local permit or DEC reference separately.", "The county letter does not mean other custodians' files have been searched."));
            case "portland-or" -> g("The Fire & Rescue tank permit trail, kept separate from DEQ decommissioning or cleanup files.",
                "the Fire & Rescue residential oil tank installation/removal permit and associated inspection records",
                c("City record", "Check bureau, permit reference, location and date; note historic city-limit coverage.", "A blank general building search does not settle the Fire & Rescue record question."),
                c("DEQ record", "Compare the state certificate or project reference separately.", "Request missing DEQ material from DEQ, not as if the City had already searched it."));
            case "south-orange-nj" -> g("A local permit supported by the available location, inspection and disposal records.",
                "the tank removal/abandonment permit, tank-location survey, inspections and waste/scrap bills of lading",
                c("Work type and location", "Compare the tank-location survey with removal or fill-in-place wording.", "Abandonment in place does not establish that the tank was removed."),
                c("Supporting attachments", "List inspection records, bills of lading and any referenced DEP final report received.", "A permit status is not delivery of those attachments."));
            case "babylon-ny" -> g("The Town Environmental tank-removal file plus any separately identified local permit documents.",
                "the Environmental storage-tank/tank-removal file and related Building permits and final inspections",
                c("Environmental file", "Match the full SCTM and physical address; retain the departmental response scope.", "A Building-only response does not settle whether Environmental holds a tank-removal record."),
                c("Other custodians", "List Town/village, Suffolk Health and DEC references separately.", "Do not extend one office's no-record reply to all record holders."));
            default -> g("A property-matched record trail showing what each document actually establishes.",
                "the tank-related permit, final inspection and removal or abandonment completion documents",
                c("Permit or index", "Compare parcel/address, issuer, reference and the proposed work.", "Permission to do work is not evidence that the work was completed."),
                c("Completion evidence", "Compare inspection date, tank location and removal versus abandonment wording.", "Keep a seller's statement or invoice separate from the agency's completion record."),
                c("Remaining scope", "List environmental references, missing attachments and the exact scope of any no-record reply.", "Stop calling the search complete while an applicable source or attachment remains unresolved."));
        };
    }
}
