package owner.buriedoiltank.data;

import java.util.List;
import owner.buriedoiltank.data.ResearchCatalog.Entry;
import owner.buriedoiltank.data.ResearchCatalog.Step;

/** Document-specific routes. Source instructions are not customer findings. */
final class RecordTasks {
    private RecordTasks() {}
    private static Step s(String title, String body, String url) { return new Step(title, body, "Official instructions / record system", url); }
    private static final String OR_GUIDE = "https://www.oregon.gov/deq/permits/Documents/FindingHeatingOilTankRecords.pdf";
    private static final String OR_PORTAL = "https://ordeq-edms-public.govonlinesaas.com/pub/pub-rcd";
    private static final String OR_REQUEST = "https://www.oregon.gov/deq/about-us/Pages/Request-Public-Record.aspx";
    private static final String NJ_NFA = "https://dep.nj.gov/srp/unregulated/nfa-letter/";
    private static final String ME = "https://www.maine.gov/dep/maps-data/data.html";
    static final List<Entry> ALL = List.of(
        new Entry("njdep-nfa-letter", "task", "new-jersey", "New Jersey · NJDEP",
            "Find an NJDEP oil tank No Further Action letter",
            "Look for the environmental closure letter, not just the municipal removal permit. Start with the incident reference and the letter's approximate date.",
            "Property address and municipality; NJDEP incident or PI number if known; approximate NFA date.",
            List.of(
                s("Check whether an NFA is the right document", "NJDEP's FAQ says an NFA is not needed where an unregulated heating oil tank was closed with no evidence of a discharge and passed municipal inspection. Do not infer those conditions from an empty search. First compare the local completion file; pursue an NFA when there is an environmental remediation case.", "https://dep.nj.gov/srp/unregulated/unregulated-faqs/"),
                s("Choose the letter route by date", "NJDEP separates older NFA retrieval from its newer online route. Letters before September 2015 go through records access; use the official NFA instructions for newer letters. This is a document lookup, not a new cleanup application.", NJ_NFA),
                s("Match the environmental reference", "Use the address, municipality and available incident or PI number in the official lookup. Keep the letter with its referenced case. Our worksheet helps you compare identifiers without uploading the property or claiming an automatic match.", "https://njems.nj.gov/DataMiner"),
                s("Request the missing letter or underlying file", "If you cannot locate the letter, describe the existing document and known case reference in an NJDEP records request. Separately obtain local removal and inspection papers. An empty search is not a finding about the property's condition.", "https://dep.nj.gov/opra/")),
            "Please provide the existing oil tank NFA letter and referenced closure documents for [property], NJDEP incident/PI [number], approximately [dates]. Please identify any missing identifiers needed to locate the file.",
            "An NFA concerns its environmental case and stated scope. It is not interchangeable with a removal permit or proof that every tank on a parcel is absent.", "find_records", List.of("njdep-tank-documents", "missing-removal-records")),
        new Entry("njdep-tank-documents", "task", "new-jersey", "New Jersey · NJDEP",
            "Find NJDEP oil tank case documents",
            "Move from a case or report reference to the documents behind it. Keep NJDEP environmental records separate from municipal construction files.",
            "Address, municipality, incident/PI number, document type and approximate dates.",
            List.of(
                s("Locate the reference", "Start with DataMiner to identify the relevant environmental reference. Record the search terms and date. A name or nearby location alone is not enough to associate a file with your property; compare the identifiers in the source.", "https://njems.nj.gov/DataMiner"),
                s("Look for the underlying document", "Check NJDEP's document access system for the identified file. Keep the actual report title, issuer and date, not only the search listing. Distinguish the report you located from attachments or final correspondence that remain missing.", "https://njems.nj.gov/DocMiner/"),
                s("Use records access for the gap", "If the attachment is unavailable, prepare a narrowly described NJDEP records request. Ask for existing documents tied to the reference rather than asking the custodian to certify the property. Follow the current official channel requirements.", "https://dep.nj.gov/opra/")),
            "Please provide [report, attachment or correspondence] for [property], incident/PI [number], during [dates]. The online reference is [reference]. Please advise of fees before chargeable work.",
            "Online availability is not a complete case inventory. Municipal permit and completion documents may need a separate local request.", "find_records", List.of("njdep-nfa-letter", "agency-record-request")),
        new Entry("nysdec-spill-documents", "task", "new-york", "New York · NYSDEC",
            "Find NYSDEC oil spill records and case documents",
            "Use the spill reference to separate an incident listing from the underlying documents. Then follow up on the precise missing file.",
            "County, municipality, address, spill number and approximate incident date.",
            List.of(
                s("Identify the spill record", "Open the DEC environmental remediation database and select the relevant spill search. Compare location and incident details before treating a result as a property match. A spill listing does not describe every residential tank at an address.", "https://extapps.dec.ny.gov/cfmx/extapps/derexternal/index.cfm"),
                s("Keep the scope of the result", "Write down the spill number, search date and document references. Separate a case status from the actual supporting reports you possess. Use the worksheet below to mark which sources you checked and which documents still need to be requested.", "https://data.ny.gov/Energy-Environment/Spill-Incidents/u44d-k5fk"),
                s("Request documents, not a new opinion", "For missing DEC-held material, use DEC's records access instructions and cite the spill reference and dates. Local removal permits, county health records and DEC files are different record sets; one agency's reply cannot clear all three.", "https://dec.ny.gov/news/foil")),
            "Please provide existing reports, closure correspondence and attachments for spill [number] at [property], during [dates]. The specific missing document is [description].",
            "No spill result does not establish that no tank or release ever existed. A reported incident is not a tank census or a current condition assessment.", "find_records", List.of("nassau-ny", "suffolk-ny")),
        new Entry("oregon-clean-decommissioning", "task", "oregon", "Oregon · DEQ",
            "Find Oregon heating oil tank clean decommissioning records",
            "Use the clean-decommissioning certificate route when looking for a registered closure without a leak project. It is separate from cleanup records.",
            "House number and street name; DEQ reference if available; approximate work year.",
            List.of(
                s("Select the certificate module", "Open Your DEQ Online Public Records, then Permits/Licenses/Certificates. Choose PLC Type HOT Clean Decommissioning and Environmental Interest HOT Decommissioning. You are looking for an existing record, not filing a new decommissioning.", OR_PORTAL),
                s("Narrow the search", "Try the house number and street name in Keywords. Review the information and document controls for a candidate result. Compare the property and document details yourself; this site does not query the portal or verify an address match for you.", OR_GUIDE),
                s("Follow up on older or missing files", "Additional records for work before April 3, 2024 may not be online. Use DEQ's public-record request route for missing material. If your paperwork identifies a leak, also use the separate leaking-HOT project route linked below.", OR_REQUEST)),
            "Please provide existing clean decommissioning records for [property], work approximately [dates], reference [number if known]. I searched [module and terms] on [date] and still need [document].",
            "A registration is not a guarantee about present site conditions. No online result does not establish that a tank was never present. Oregon assistance here is self-service only.", "find_records", List.of("oregon-leaking-tank-records", "missing-removal-records")),
        new Entry("oregon-leaking-tank-records", "task", "oregon", "Oregon · DEQ",
            "Find Oregon leaking heating oil tank and cleanup records",
            "Follow the leaking-HOT project and its documents. Do not substitute a clean-decommissioning search for a cleanup file.",
            "DEQ LUST number if known, house number and street name, approximate incident year.",
            List.of(
                s("Choose the project route", "In Your DEQ Online Public Records, choose Projects and the Leaking Heating Oil Tank project type. This is a different record family from clean-decommissioning certificates. Keep the project reference alongside any files you obtain.", OR_PORTAL),
                s("Read the available project material", "Search by DEQ LUST number or address. The information control shows project details; the PDF control opens available documents. Treat these as candidate records until you compare their identifiers with the property you are researching.", OR_GUIDE),
                s("Prepare the missing-document request", "Record which report or closure correspondence is missing and follow DEQ's records request instructions. Do not describe a request awaiting response as a completed file search. Our worksheet keeps that pending status distinct from a delivered document.", OR_REQUEST)),
            "Please provide [cleanup report or correspondence] for [property], DEQ LUST/project [number], during [dates]. Documents already located: [list]. Missing documents: [list].",
            "Case status and a document search are not safety determinations. Active oil or strong odor needs prompt official/professional response, not this paperwork workflow. Self-service only.", "find_records", List.of("oregon-clean-decommissioning", "conflicting-tank-documents")),
        new Entry("seattle-residential-tank-records", "task", "washington", "Seattle city · Fire Department",
            "Find Seattle residential oil tank decommissioning records",
            "Search Seattle's residential permit records, then check whether the follow-up tank information is actually present. This route covers Seattle city, not all Washington.",
            "Seattle property address, permit number if known, approximate decommissioning year.",
            List.of(
                s("Open the residential dataset", "Seattle's residential UST dataset concerns Fire Department decommissioning permits, code 6103. Search the address and inspect permit number and date issued. This is not the commercial UST records route or a statewide tank inventory.", "https://data.seattle.gov/Built-Environment/Underground-Storage-Tank-UST-Records-Residential/xvj2-ai6y"),
                s("Check the follow-up fields", "Compare date and type decommissioned with the permit details. The dataset explains that incomplete tank information indicates the required follow-up report has not been received by SFD. An issued permit alone must not become a completed-removal claim.", "https://data.seattle.gov/Built-Environment/Underground-Storage-Tank-UST-Records-Residential/xvj2-ai6y"),
                s("Ask for the missing underlying record", "SFD's residential records begin in 1996; earlier work may have no SFD record. Use the City's public records channel for a specific permit or missing follow-up file. Keep the search result and the agency's eventual response separate.", "https://www.seattle.gov/public-records/public-records-request-center")),
            "Please provide the residential heating oil tank decommissioning permit and follow-up report for [property], permit [number], approximately [dates]. The public dataset is missing [field or document].",
            "Missing data is not evidence of a tank-free property. A permit record is not a present-condition inspection. Seattle guidance is self-service only; no Washington-wide research service is offered.", "find_records", List.of("missing-removal-records", "records-or-tank-sweep")),
        new Entry("maine-tank-spill-records", "task", "maine", "Maine · DEP",
            "Find Maine tank registration and oil spill files",
            "Choose TankSmart for registration information and the DEP document portals for finalized spill or tank files. They answer different questions.",
            "Town, address, facility or tank reference, spill number and approximate dates.",
            List.of(
                s("Choose registration or spill documents", "For registration, choose Search Registered Tanks in TankSmart, not Get Certified. The search offers registration number, facility name, town/city and street address. For spill reports, use the separate DEP document route in the next step; registration does not include every cleanup file.", "https://apps.web.maine.gov/cgi-bin/dep/tanksmart/index.cgi"),
                s("Use the document query", "In the linked document portal, enter a Query Value such as town, facility or spill number, then Search. An asterisk expands a query. Downloadable HOSS tables are a separate technical resource, not a substitute for the document you need.", ME),
                s("Ask the file room about unscanned records", "DEP says not every file is scanned. If the document is missing online, ask the file room about the specified file and arrange the appropriate review. Keep the requested file list and the delivered documents separate in your worksheet.", ME)),
            "Please help locate [tank or finalized spill documents] for [property/town], reference [number], during [dates]. The online search returned [result]; the missing file is [description].",
            "Registered tanks and finalized spill files are not a complete inventory of every residential tank. This is self-service guidance, not an offer of Maine research assistance.", "find_records", List.of("agency-record-request", "records-or-tank-sweep")),
        new Entry("connecticut-deep-spill-documents", "task", "connecticut", "Connecticut · DEEP",
            "Find Connecticut DEEP oil spill reports and documents",
            "Locate the underlying spill report by property or case reference, then distinguish missing online documents from a record request still awaiting response.",
            "Town, street address, agency/case ID, document type and approximate document dates.",
            List.of(
                s("Search the document portal", "The DEEP document portal offers Town, Street Address, Agency ID, Agency Program and document-type fields. Start with the address or known case reference and narrow the result. You are retrieving agency documents, not submitting a new spill report.", "https://filings.deep.ct.gov/DEEPDocumentSearchPortal/"),
                s("Separate incident data from the file", "DEEP's FOIA guidance distinguishes spill datasets from the associated reports. Use a listing to identify a reference, then obtain the document itself. An incident entry is not evidence that the requested report or every attachment is available online.", "https://portal.ct.gov/deep/about/foia-requests"),
                s("Follow the missing-file route", "DEEP directs users through online search, its Records Center and then a specific FOIA request when needed. Prepare one property and the missing record category. Follow current appointment or request requirements; this worksheet does not book or send anything.", "https://portal.ct.gov/deep/about/foia-requests")),
            "Please provide existing spill reports and [specific attachments] for [property; town], case/agency ID [number], during [dates]. I checked [source] on [date] and still need [document].",
            "A spill-document search is not a complete residential tank inventory or a safety finding. Local tank work may require a municipal file. Connecticut guidance is self-service only.", "find_records", List.of("agency-record-request", "records-or-tank-sweep"))
    );
}
