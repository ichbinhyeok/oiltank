package owner.buriedoiltank.data;

import java.time.LocalDate;
import java.util.List;

/** Editorial search aids. Source URLs are always taken from the resolved catalog entry. */
public final class RecordSearchGuide {
    private RecordSearchGuide() {}
    public static final LocalDate UPDATED = LocalDate.of(2026, 10, 11);
    public record Answer(String question, String answer) {}
    public record Guide(String searchTitle, int startStep, String action, String expected, String fallback,
                        List<Answer> answers) {}
    private static Answer a(String question, String answer) { return new Answer(question, answer); }
    private static Guide g(String title, int step, String action, String expected, String fallback, Answer... answers) {
        return new Guide(title, step, action, expected, fallback, List.of(answers));
    }
    public static Guide forEntry(ResearchCatalog.Entry entry) {
        return switch (entry.slug()) {
            case "njdep-nfa-letter" -> g("NJDEP NFA letter search: oil tank closure records", 1, "Open NJDEP NFA instructions",
                "An actual No Further Action letter tied to the incident or PI reference—not a municipal permit status.",
                "Check the letter date and case reference. For an older or unavailable letter, use NJDEP records access; obtain local completion papers separately.",
                a("Is a removal permit the same as an NFA letter?", "No. A local permit concerns the described tank work; an NFA concerns an environmental case. Do not request a new NFA merely because a municipal record is missing."),
                a("What if I do not know the incident number?", "Start with the address and municipality in DataMiner and check existing correspondence for a PI or incident reference. Verify the property match before requesting the letter."));
            case "njdep-tank-documents" -> g("NJDEP DataMiner & DocMiner: oil tank case documents", 0, "Open NJDEP DataMiner",
                "A case reference from DataMiner, then the report or correspondence available in DocMiner or through records access.",
                "A listing is not delivery. Name the report, dates and PI/incident number in a request for the missing attachment.",
                a("DataMiner or DocMiner: which should I use?", "Use the case/report reference to narrow the search, then look for the actual document. Preserve both the index reference and the files downloaded; they serve different purposes."),
                a("Does NJDEP hold the town's removal certificate?", "Do not assume so. Follow the local record route for municipal permits, inspections and completion papers, and keep the environmental case separate."));
            case "nysdec-spill-documents" -> g("NYSDEC spill database search & case document requests", 0, "Open NYSDEC environmental search",
                "The matching spill number and incident details, followed by the specific reports or closure correspondence you need.",
                "Check DEC's online document routes before FOIL. Cite the spill number and missing record category; a spill status is not the report itself.",
                a("Does a closed spill listing give me the closure report?", "Not necessarily. Save the spill reference and request or download the actual correspondence and supporting reports. Record the exact documents obtained."),
                a("Does a blank spill search mean there was no oil tank?", "No. This is a reported-incident search, not a complete residential tank inventory. Search the appropriate local permit or completion file separately."));
            case "oregon-clean-decommissioning" -> g("Oregon DEQ oil tank decommissioning records & certificates", 0, "Open Your DEQ Online records",
                "The site's HOT clean-decommissioning certificate and any available supporting file, not a contractor's license.",
                "Use house number and street name. Record an absent PDF separately from an absent listing; request older or missing files from DEQ.",
                a("Which filters find a clean decommissioning record?", "Choose Permits/Licenses/Certificates, PLC Type HOT Clean Decommissioning and Environmental Interest HOT Decommissioning. Use the separate Projects route for a leaking tank."),
                a("What about work before April 3, 2024?", "DEQ says additional older records may not be online. Keep the approximate work year and request the specific missing material rather than treating an empty portal as no tank."));
            case "oregon-leaking-tank-records" -> g("Oregon DEQ leaking oil tank records: LUST & cleanup files", 0, "Open Your DEQ Online projects",
                "The leaking-HOT project reference, cleanup report and any final correspondence actually available for that site.",
                "Search the LUST number or address in Projects. If a report or attachment is absent, prepare a DEQ records request tied to that project.",
                a("Why did the clean-decommissioning search return nothing?", "A leaking heating oil tank uses a different record family: Projects → Leaking Heating Oil Tank. A clean-certificate search does not complete a cleanup-file search."),
                a("Is project status enough for a buyer's document file?", "Keep the underlying reports and correspondence as well as the status. Match the project number, location and dates; this guide does not determine whether the property is safe."));
            case "seattle-residential-tank-records" -> g("Seattle oil tank decommissioning records: SFD lookup", 0, "Search Seattle residential UST records",
                "A Seattle Fire permit reference plus its reported decommissioning date, method and tank details.",
                "No row, blank follow-up fields and a missing document are different gaps. Use the City records channel for the underlying permit/report; pre-1996 work may have no SFD record.",
                a("Which fields should I check in the Seattle dataset?", "Compare Address, Permit Number and Date Issued first. Then inspect Date Decommissioned, Type Decommissioned, Tank Size, Fill Material and Company. A permit issue date alone is not a work-completion date."),
                a("Does decommissioned mean the tank was removed?", "Do not assume that. Read the Type Decommissioned and supporting report. Decommissioning in place and removal are different actions; fill-material information is not a present-condition inspection."),
                a("Does this cover all King County addresses or commercial tanks?", "No. This dataset is Seattle residential permit code 6103. Use the appropriate city or unincorporated-county route outside Seattle; commercial UST records go through the City Public Records Request Center."));
            case "maine-tank-spill-records" -> g("Maine TankSmart search & DEP oil spill records", 0, "Open TankSmart tank search",
                "A registration reference from TankSmart, or a finalized spill/tank document from the separate DEP document portals.",
                "Choose the record family first. Search a town, reference or facility in the relevant portal; contact the file room about specified unscanned material.",
                a("Should I choose Get Certified in TankSmart?", "Not for property-record research. Use Search Registered Tanks for registration information. Operator training/certification is a different task."),
                a("Where is the actual spill report?", "Use DEP's finalized spill-report or spill-project document links, not just the registration search or HOSS tables. Some files remain unscanned and require file-room assistance."));
            case "connecticut-deep-spill-documents" -> g("CT DEEP spill reports: document portal & REACT search", 0, "Open DEEP document search",
                "The case-matched spill report and attachments, with cleanup documents checked in the appropriate DEEP portal.",
                "Search by address or case ID. Check REACT for 2026 cleanup uploads, then use the Records Center and specific FOIA route for the remaining gap.",
                a("Document Search Portal or REACT?", "DEEP directs older submitted cleanup documents to its Document Search Portal. Its 2026 guidance adds REACT for cleanup status and recent uploads; check the same address/case reference rather than assuming every file sits in one system."),
                a("Is an incident dataset row the spill report?", "No. Use the row to identify the case, then retrieve the report and associated documents. A missing online file and a pending agency request are separate outcomes."));
            case "brookhaven-ny" -> g("Brookhaven oil tank records: permits, inspections & FOIL", 1, "Open Brookhaven FOIL instructions",
                "Town-held tank permits, inspection results and completion attachments, with each missing document named.",
                "If a pre-September 28, 2026 application is missing from Digital Services, use Building's old-application help route. Use FOIL for existing files, not a new work application.",
                a("My old permit is missing after the portal move. What now?", "Building provides a help form for applications submitted before September 28, 2026 that cannot be found or updated. Keep the old permit reference and work dates. A migration gap is not proof the work was never permitted."),
                a("Does Brookhaven FOIL include Suffolk Health or DEC records?", "No. The Town explicitly excludes those agencies' records. Track Town construction, Suffolk Health and NYSDEC environmental files as separate searches."),
                a("What should the request name?", "Ask for existing tank removal or abandonment permits, final inspection results and completion attachments for one identified property and date range. Request the missing attachment, not a safety opinion."));
            case "new-rochelle-ny" -> g("New Rochelle oil tank removal inspection records", 1, "Open New Rochelle FOIL instructions",
                "The Fire Department's existing oil tank removal inspection/tank-status record and separately held Building attachments.",
                "Name Fire's tank-status record in the Clerk's FOIL request. A general property-card or Building-only search may leave that file unresolved.",
                a("Why request Fire Department records?", "New Rochelle Fire describes documenting tank status during oil tank removal inspections. That makes its inspection record a specific target—not a guarantee that a record exists for every historic removal."));
            case "king-county-wa" -> g("King County oil tank records: unincorporated permit files", 0, "Open King County permit-record research",
                "The County-held tank-work permit and available inspection/certification attachments for the correct jurisdiction and time period.",
                "Confirm city limits and annexation dates first. Seattle and Bellevue generally require their own city searches; historic County custody can differ.",
                a("Can I use this route for a Seattle mailing address?", "Only after checking legal jurisdiction and the work date. A mailing city does not establish County permit custody. King County directs city-limit properties to the city, while retaining some records from before annexation."));
            case "bellevue-wa" -> g("Bellevue oil tank decommissioning report records", 1, "Open Bellevue public-record instructions",
                "The residential oil tank decommissioning report held by Fire Prevention, not just a general building permit entry.",
                "Request the report by address, approximate completion date and contractor if known. Keep Fire records separate from Development Services permit files.",
                a("What is the specific Bellevue document to request?", "Handout F-07 identifies a decommissioning report returned to Fire and retained as a public record. Ask for that existing report; the handout alone does not prove it was filed for your property."));
            case "tacoma-wa" -> g("Tacoma oil tank decommissioning certificate records", 1, "Open Tacoma public-record instructions",
                "The Fire Prevention decommissioning certification, where recorded, rather than only a historical building index hit.",
                "Ask for the Fire-held certification. The Health Department says it does not keep small heating-oil tank records; do not treat that office's blank as a complete search.",
                a("Should I start with Pierce County Health tank records?", "Not for a small home heating-oil tank: Tacoma-Pierce County Health says it does not keep those records. Tacoma Fire's FAQ identifies recorded decommissioning certificates as a separate target."));
            default -> ExpandedRecordRoutes.profile(entry.slug()) == null ? null : ExpandedRecordRoutes.profile(entry.slug()).search();
        };
    }
    public static boolean updated(ResearchCatalog.Entry entry) { return forEntry(entry) != null; }
}
