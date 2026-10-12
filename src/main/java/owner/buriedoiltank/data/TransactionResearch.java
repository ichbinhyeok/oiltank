package owner.buriedoiltank.data;

import java.util.List;
import owner.buriedoiltank.data.ResearchCatalog.Entry;
import owner.buriedoiltank.data.ResearchCatalog.Step;

/** Distinct transaction-document questions, not duplicated city landing pages. */
public final class TransactionResearch {
    private TransactionResearch() {}
    private static final String NJ = "https://dep.nj.gov/srp/unregulated/unregulated-faqs/";
    private static final String NY = "https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/underground-heating-oil-tanks-homeowner-guide";
    private static final String OR = "https://www.oregon.gov/deq/tanks/pages/hot-buying-or-selling.aspx";
    private static final String OR_RECORDS = "https://www.oregon.gov/deq/permits/Documents/FindingHeatingOilTankRecords.pdf";
    private static final String SEA = "https://data.seattle.gov/Built-Environment/Underground-Storage-Tank-UST-Records-Residential/xvj2-ai6y";
    private static Step s(String title, String body, String label, String url) { return new Step(title, body, label, url); }
    private static Entry e(String slug, String locality, String title, String summary, String identifiers, List<Step> steps, String request, String boundary, String... related) {
        return new Entry(slug, "problem", "", locality, title, summary, identifiers, steps, request, boundary, "interpret_documents", List.of(related));
    }
    public static final List<Entry> ENTRIES = List.of(
        e("oil-tank-filled-with-sand", "Filled-in-place tank", "Buying a house with an oil tank filled with sand: records to check",
            "A buried tank filled with sand or foam was not necessarily removed. Check the work method, inspection and sampling file before treating a closure claim as a complete answer.",
            "Exact closure wording; work year; tank location and capacity; contractor; permit, certificate and sampling references.",
            List.of(
                s("Read the method, not just the word closed", "Separate emptied, removed and closed in place. NYSDEC describes closure in place as a different option from removal. Match the report's action and tank location to the seller's claim; a fill-material entry does not establish which supporting work or testing was performed.", "NYSDEC tank-closure context", NY),
                s("Locate the work and sampling attachments", "Ask for the contractor's closure report and the actual available inspection and sampling documents. Oregon's guidance distinguishes emptying, decommissioning and certification; use that distinction only within Oregon, not as a universal document requirement for other states.", "Oregon DEQ documentation distinctions", OR),
                s("Keep past completion separate from present conditions", "NJDEP expressly says permitted abandonment does not guarantee a tank never leaked. Records describe their stated scope and date. If physical condition or contamination remains unresolved, ask a qualified professional rather than treating sand, foam or a closed status as a safety finding.", "NJDEP abandonment FAQ", NJ)),
            "Please provide the existing closure-in-place work report, final inspection and available sampling/laboratory attachments for [property], tank [location/capacity], work [year], reference [number]. The file I hold says [exact wording]; missing material is [list].",
            "This is a document-research route, not advice to buy or walk away. Filled in place does not mean removed or certify absence of contamination. We do not assess tank condition.", "seller-says-tank-removed", "oregon-clean-decommissioning", "seattle-residential-tank-records", "oil-tank-soil-test-records"),
        e("oil-to-gas-tank-records", "Former oil heat", "Converted to gas but no oil tank removal records: what to check",
            "Gas or heat-pump installation papers do not establish what happened to an old heating-oil tank. Trace the conversion and tank-work files separately.",
            "Conversion date and installer; prior oil-system clues; alleged tank work date; tank permit/report reference; legal jurisdiction at the work date.",
            List.of(
                s("Separate the two jobs", "Record what the conversion invoice actually describes. Oregon's unused-tank guidance addresses the former underground tank separately from the new heating source. Look for an explicit tank-work reference rather than reading removal into furnace or heat-pump installation language.", "Oregon DEQ buying/selling guidance", OR),
                s("Trace tank-work records with the correct custodian", "Search the local office for tank-work permits and completion attachments. Seattle's residential dataset, for example, records permitted decommissioning and follow-up details, not every historic heating conversion. Its coverage limits do not apply to all other cities.", "Seattle residential tank-record scope", SEA),
                s("Name the physical question left open", "A blank record search does not settle whether an unrecorded tank remains. Oregon recommends a qualified expert when tank presence is unknown. Keep documentary research and any owner-authorized physical investigation as separate tracks with their own scope and findings.", "Oregon DEQ unknown-tank guidance", OR)),
            "Please provide existing oil tank removal/closure permits, inspections and completion attachments for [property], approximately [years]. Heating conversion occurred in [year], but I have not located a separate tank-work record. Known reference: [number].",
            "Conversion, capped pipes and missing records are not tank-free findings. Do not enter, open or investigate a tank yourself through this workflow. Professional physical assessment may still be needed.", "records-or-tank-sweep", "missing-removal-records", "king-county-wa"),
        e("oil-tank-open-permit", "Permit without final evidence", "Oil tank removal permit not closed: find the final inspection record",
            "An issued tank permit is not proof that work was finished. Locate its final disposition, inspection result and completion attachments before interpreting an online status.",
            "Permit number; issuing office; issue date and stated work; expected final inspection; contractor; separate environmental case number if one exists.",
            List.of(
                s("Separate issue date from work completion", "Keep each milestone on its own line. Seattle's dataset publishes Date Issued separately from Date Decommissioned and other follow-up details. Missing follow-up information is a report gap within that dataset, not a completed-removal finding or a nationwide permit rule.", "Seattle permit and follow-up fields", SEA),
                s("Ask the issuer for the final disposition", "Request the existing final inspection, completion attachment or status explanation tied to the permit. NJDEP directs questions about unregulated heating-oil tank removal permits to the municipality; an environmental database alone does not resolve the local permit's disposition.", "NJDEP municipal permit FAQ", NJ),
                s("Follow an environmental reference separately", "If the local file refers to a discharge or remediation case, preserve the reference and obtain the relevant environmental correspondence. NJDEP distinguishes remediation-related NFA papers from clean municipal closure. Do not invent a requirement for an NFA merely because an online permit has no final entry.", "NJDEP NFA versus municipal closure", NJ)),
            "Please provide the existing final inspection, completion documents and current recorded disposition for tank-work permit [number] at [property], issued [date]. The online record shows [status]. Please identify any missing attachment or separate referenced case.",
            "This tool does not close permits, decide code compliance or set a completion deadline. An issuer must clarify its record, and a transaction professional must address any purchase or sale consequence.", "conflicting-tank-documents", "agency-record-request", "njdep-nfa-letter"),
        e("nfa-vs-removal-certificate", "Two different document scopes", "Oil tank NFA letter vs removal certificate: which record is missing?",
            "An NJDEP No Further Action letter addresses a remediation case; local removal or completion papers address tank work. A clean municipal UHOT closure does not automatically need an NFA.",
            "State and municipality; actual document title and issuer; work method; local permit; incident/PI or other environmental case reference, if applicable.",
            List.of(
                s("Check whether an NFA belongs to this case", "NJDEP FAQ 15 says an NFA is not needed when a UHOT was closed with no evidence of discharge and passed municipal inspection. This is not a determination that your property meets those conditions. Establish the documented case history before labeling an NFA as missing.", "NJDEP UHOT FAQ 15", NJ),
                s("Retrieve the environmental letter by its reference", "Where an NJDEP remediation case exists, use its incident or PI reference to locate the issued letter and supporting reports. A database status is not the letter itself. Keep local permit and inspection documents in a separate source line rather than treating the NFA as all-purpose tank paperwork.", "NJDEP UHOT case and NFA guidance", "https://dep.nj.gov/srp/unregulated/"),
                s("Use the local equivalent, not a nationwide certificate name", "Document names differ. Nassau offers a small-tank removal/abandonment verification route; Oregon separates clean-decommissioning records from leak projects. Follow the actual issuer's scope rather than demanding an NJ-style NFA for every tank in every state.", "Nassau small-tank verification route", "https://www.nassaucountyny.gov/3027/Environmental-Health")),
            "Please provide [local completion/verification record OR issued environmental closure letter] for [property], reference [permit or environmental case], approximately [dates]. I hold [document/issuer]; the specific missing record is [description].",
            "An NFA is not a guarantee about every tank, all property conditions or future events. This comparison does not establish legal sufficiency, eligibility for a letter or whether a transaction should proceed.", "njdep-nfa-letter", "nassau-ny", "oregon-clean-decommissioning", "conflicting-tank-documents"),
        e("oil-tank-soil-test-records", "Missing report attachments", "Oil tank soil test records: find sampling reports and lab attachments",
            "A removal invoice saying soil tested is not the underlying sampling and laboratory report. Locate the referenced evidence and have its adequacy reviewed by a qualified professional.",
            "Report title and author; tank location; sampling/work date; case or permit number; named laboratory; referenced appendices and sample identifiers.",
            List.of(
                s("Identify the report behind the summary", "Separate the work invoice, technical report and laboratory attachments. Oregon's guidance distinguishes emptying from certified decommissioning with soil sampling. Do not turn a short contractor statement or missing attachment into a conclusion that sampling met a particular standard.", "Oregon DEQ certification context", OR),
                s("Follow the correct project or certificate record", "In Oregon, the clean-decommissioning PLC search and leaking-HOT Projects search target different files. Preserve the reference and request specifically missing reports or laboratory appendices; older additional material may not be online. Other states use their own document custodians.", "Oregon DEQ record-finding instructions", OR_RECORDS),
                s("Give the full file to the right reviewer", "Ask a qualified environmental professional to review the actual sampling locations, methods, dates and laboratory results in context. NYSDEC's homeowner guide discusses environmental assessment separately from a tank's status. We locate and organize documents, not determine contamination or prescribe sampling.", "NYSDEC environmental assessment context", NY)),
            "Please provide the existing sampling report, sample-location plan, laboratory results and referenced appendices associated with [tank-work report/case number] at [property], dated [date]. Material received: [list]. Missing attachments: [list].",
            "No sample result is interpreted by this checklist. It does not determine whether a tank leaked, whether sampling is sufficient or whether a property is safe. Use a qualified environmental professional for those questions.", "oregon-leaking-tank-records", "njdep-tank-documents", "conflicting-tank-documents")
    );
    public static boolean isNew(String slug) { return ENTRIES.stream().anyMatch(e -> e.slug().equals(slug)); }
}
