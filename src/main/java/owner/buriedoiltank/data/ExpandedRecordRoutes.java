package owner.buriedoiltank.data;

import java.util.List;
import owner.buriedoiltank.data.ResearchCatalog.Entry;
import owner.buriedoiltank.data.ResearchCatalog.Step;

/** Researched public custody routes, never property findings or nationwide service acceptance. */
public final class ExpandedRecordRoutes {
    private ExpandedRecordRoutes() {}
    public record Profile(Entry entry, RecordSearchGuide.Guide search, RecordEvidence.Guide evidence, String family) {}
    private static Step s(String title, String body, String label, String url) { return new Step(title, body, label, url); }
    private static RecordEvidence.Check c(String document, String compare, String gap) { return new RecordEvidence.Check(document, compare, gap); }
    private static RecordSearchGuide.Answer a(String question, String answer) { return new RecordSearchGuide.Answer(question, answer); }
    private static Entry e(String slug, String kind, String state, String place, String title, String summary,
                           String identifiers, List<Step> steps, String request, String boundary, String... related) {
        return new Entry(slug, kind, state, place, title, summary, identifiers, steps, request, boundary, "find_records", List.of(related));
    }
    private static Profile p(Entry entry, String family, int start, String action, String target, String fallback,
                             String scope, RecordEvidence.Check first, RecordEvidence.Check second,
                             RecordSearchGuide.Answer... answers) {
        return new Profile(entry, new RecordSearchGuide.Guide(entry.title(), start, action, target, fallback, List.of(answers)),
            new RecordEvidence.Guide(target, scope, List.of(first, second)), family);
    }
    private static final String MASS = "https://www.mass.gov/find-out-about-a-contaminated-property";
    private static final String MASS_REQUEST = "https://www.mass.gov/guides/making-a-public-records-request-at-massdep";
    private static final String VT = "https://anrweb.vt.gov/dec/ert/";
    private static final String RI = "https://dem.ri.gov/environmental-protection-bureau/customer-and-technical-assistance/file-review";
    private static final String RI_LIST = "https://dem.ri.gov/environmental-protection-bureau/land-revitalization-and-sustainable-materials-management/inventories";
    private static final String MD = "https://mes-mde.mde.state.md.us/caseinformation/";
    private static final String MD_OPEN = "https://mde.maryland.gov/Pages/Open-MDE.aspx";
    private static final String VA = "https://www.deq.virginia.gov/land-waste/petroleum-tanks";
    private static final String PA = "https://www.pa.gov/agencies/dep/programs-and-services/land/storage-tanks/storage-tank-data";
    private static final String PA_REVIEW = "https://www.pa.gov/services/dep/request-general-or-informal-file-review-of-department-of-environmental-protection-records";
    private static final String ECOLOGY = "https://apps.ecology.wa.gov/cleanupsearch/";
    private static final String PLIA = "https://plia.wa.gov/about-plia/public-records-requests/";
    private static final String PBS = "https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/forms-instructions/pbs-registration-renewal-faq";
    private static final String DEC_HOME = "https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/underground-heating-oil-tanks-homeowner-guide";
    private static final String DEC_REQUEST = "https://dec.ny.gov/news/foil";
    private static final String CT = "https://portal.ct.gov/deep/about/foia-requests";
    private static final String BOSTON = "https://www.boston.gov/departments/fire-prevention/special-hazards";
    private static final String BOSTON_RECORDS = "https://www.boston.gov/departments/public-records";
    private static final String NEWTON = "https://www.newtonma.gov/government/fire/fire-prevention/forms-and-applications";
    private static final String NEWTON_RECORDS = "https://www.newtonma.gov/government/city-clerk/public-records-requests";
    private static final String WELLESLEY = "https://www.wellesleyma.gov/791/Permits-and-Fees";
    private static final String WELLESLEY_RECORDS = "https://www.wellesleyma.gov/1813/Public-Records";
    private static final String SCARSDALE = "https://www.scarsdale.gov/675/Applications-for-Permits";
    private static final String SCARSDALE_FOIL = "https://www.scarsdale.gov/519/FOIL---Request-Public-Records";
    private static final String GREENWICH = "https://www.greenwichct.gov/480/Fire-Marshals-Division";
    private static final String WESTPORT = "https://www.westportct.gov/government/departments-a-z/building-department";
    private static final String STAMFORD = "https://www.stamfordfire.com/freedom-of-information-records-request-information/";
    private static final String WILTON = "https://www.wiltonct.gov/planning-zoning-department/pages/online-permitting-and-licensing-portal-and-historical-records";

    public static final List<Profile> PROFILES = List.of(
        p(e("massdep-oil-release-files", "task", "massachusetts", "Massachusetts · MassDEP",
            "MassDEP oil spill records: RTN lookup & file viewer",
            "Find the Release Tracking Number, then retrieve the cleanup submittals behind a reported heating-oil release. Fire removal records are a separate file.",
            "Town, street address and former address, Release Tracking Number (RTN), approximate release date and the report or closure document sought.",
            List.of(
                s("Find the release reference, not just a map pin", "MassDEP's contaminated-property service covers reported releases, including fuel-oil delivery spills. Search the address or town and preserve the full Release Tracking Number. One property can have more than one release reference; compare the reported event and location before choosing a file.", "MassDEP waste site / release lookup", MASS),
                s("Open the submittals behind the RTN", "Follow the service's eDEP Form/Submittals or file-viewer link for the RTN. List the actual reports, attachments and final correspondence you can open. An online cleanup status does not deliver every appendix, and a report about one release does not document all tank work at the property.", "MassDEP document lookup instructions", MASS),
                s("Request the named missing material", "Use MassDEP's records instructions for a specified report, missing attachment or older file, naming the RTN and dates. The agency identifies program and regional coordinators. Request municipal Fire tank-work papers separately; a state release-file request is not a request to the local Fire Department.", "MassDEP public-record access", MASS_REQUEST)),
            "Please provide existing [report/closure correspondence/appendix] for [address; town], RTN [full number], dated [range]. I located [files] online and still need [named material]. Please identify any older-file or regional custody referral.",
            "A reportable-release search is not a list of every home oil tank. No RTN does not establish no tank or no past release. Massachusetts guidance is self-service only.", "boston-ma", "newton-ma", "wellesley-ma", "conflicting-tank-documents"),
            "environmental", 0, "Open MassDEP release lookup", "The matching RTN plus the actual cleanup reports and final correspondence, not a status alone.",
            "Compare all relevant RTNs at the address. Ask the regional/program records coordinator for the named missing attachment; use Fire for removal paperwork.",
            "the cleanup reports, named appendices and final correspondence for the specified MassDEP RTN",
            c("Release reference", "Match town, address, RTN and incident dates; preserve separate releases.", "A nearby map pin or a different RTN is not a property match."),
            c("Submittal and closure", "Check report date, revision and named attachments against files actually obtained.", "A status or transmittal receipt is not the underlying report."),
            a("Is an RTN a tank removal permit?", "No. It identifies a reported environmental release. Ask the local Fire record holder for tank installation/removal and inspection documents."),
            a("What if there are several RTNs?", "Keep them separate until the dates, incident descriptions and reports establish their relationship. Do not select only the most reassuring status.")),
        p(e("vermont-ert-oil-records", "task", "vermont", "Vermont · ANR / DEC",
            "Vermont ERT oil spill & hazardous-site records",
            "Use the Environmental Research Tool's separate Spills, Hazardous Sites and UST/AST searches. A tank-facility search alone can miss a spill file.",
            "Town and address, historic site/facility name, spill report year/number, hazardous-site number and approximate event dates.",
            List.of(
                s("Choose the ERT program before searching", "ERT's home search spans programs, while its menu separates Spills, Hazardous Sites and UST/AST. Start with the event you are researching. A household oil spill and a registered storage facility are not interchangeable records, so keep the program name with every reference you find.", "Vermont Environmental Research Tool", VT + "Default.aspx"),
                s("Search the incident and site separately", "Spills offers address, town and date/year filters; its unfiltered display is only the most recent 500 incidents. Hazardous Sites has its own site-number and address fields. An older spill will not be resolved by scanning the recent list. Preserve both references when an incident led to a separate managed-site file.", "ERT spill search", VT + "Spills.aspx"),
                s("Retrieve documents or narrow a records request", "Open the relevant program record and review the documents it actually exposes. For material absent online, use ANR's public-record instructions and specify DEC's program, reference and missing report. Do not send an environmental request to the Secretary of State merely because it offers a generic records form.", "ANR public-record instructions", "https://anr.vermont.gov/about-us/public-records")),
            "Please provide existing [spill report/site investigation/closure correspondence] for [address; town], ERT [program and reference], [dates]. Online material located: [list]. Missing material: [specific files].",
            "ERT's UST/AST inventory has program and registration limits; it is not a complete residential tank register. An empty recent-spill list is not an all-years search. Self-service only.", "maine-tank-spill-records", "agency-record-request"),
            "environmental", 0, "Open Vermont ERT", "A program-labelled spill/site reference and its available reports, with registration information kept separate.",
            "Use the year and address filters rather than the recent-500 display. Check Hazardous Sites separately, then request the missing DEC-held report.",
            "the spill investigation, hazardous-site reports and closure correspondence for the specified ERT references",
            c("ERT program", "Record whether the reference is a spill, hazardous site or tank facility.", "A facility identifier is not necessarily the spill or cleanup case number."),
            c("Historical coverage", "Keep the searched years and the actual reports retrieved.", "A blank recent list or unreturned document remains unresolved."),
            a("Why does the recent spill list not show an old incident?", "The unfiltered Spills page displays the most recent 500. Use its town, address and year/date fields before drawing any conclusion."),
            a("Should I also search Hazardous Sites?", "When the known event may have led to a managed cleanup site, check that separate ERT program and retain its distinct identifier.")),
        p(e("rhode-island-dem-oil-files", "task", "rhode-island", "Rhode Island · DEM",
            "Rhode Island DEM oil spill, UST & LUST file review",
            "Choose Emergency Response, UST or LUST custody before requesting a DEM file. A site inventory is a lead, not delivery of the reports.",
            "Street address, municipality, historic facility name, incident/facility/site reference, dates and the exact documents needed.",
            List.of(
                s("Use inventories to identify the file", "DEM's site-inventory page separates record programs and published lists. Check which list and vintage supplied a candidate reference. A regulated UST or cleanup inventory is not a complete home-tank history, and a nearby site should not be assigned to the subject property without comparing identifiers.", "DEM site inventories", RI_LIST),
                s("Select the correct file-review program", "DEM's file-review coordinator lists Emergency Response oil spills and aboveground registrations separately from UST and LUST files. Describe the incident and record family in the request. Asking for all tank paperwork without naming the program can leave the actual oil-spill response file unresolved.", "DEM file-review program distinctions", RI),
                s("Request inspection or copies of the missing records", "Follow DEM's current file-review instructions and linked request form for the identified program. State whether you need inspection or copies and name the report and date range. Keep scheduling, acknowledgment, fees and documents delivered as separate events; no appointment or message is sent from this page.", "DEM records request and file review", RI)),
            "Please provide access to existing [Emergency Response oil-spill / UST / LUST] records for [address; town], [reference], [dates], specifically [reports and correspondence]. Please identify the correct program if these records are held elsewhere.",
            "Program inventories and file-review coordination do not prove a file exists for every residential tank. Agency access requirements and charges apply independently. Self-service only.", "massdep-oil-release-files", "agency-record-request"),
            "environmental", 1, "Open DEM file-review instructions", "The specified program's existing incident/cleanup reports, not just an inventory line.",
            "Ask the coordinator which program was searched. Keep an Emergency Response oil-spill file separate from UST/LUST registration and cleanup files.",
            "the specified DEM Emergency Response, UST or LUST reports and related final correspondence",
            c("Program and reference", "Compare town, location, event dates and the DEM program named in the reply.", "A UST-only search may not answer the Emergency Response question."),
            c("File delivery", "List the report and attachments actually inspected or received.", "A scheduled review or acknowledgment is not document delivery."),
            a("Which DEM program should an oil-spill request name?", "DEM lists Emergency Response for oil-spill responses, alongside separate UST and LUST files. Use the event and known reference to identify the appropriate program.")),
        p(e("maryland-oil-control-case-files", "task", "maryland", "Maryland · MDE Oil Control",
            "Maryland MDE Oil Control case search & records",
            "Find an Oil Control case by partial address or case number, include closed cases, then pursue the underlying documents through MDE records access.",
            "Case number or facility ID, partial street address, county, location name and approximate incident/report dates.",
            List.of(
                s("Search partial values, not wildcard characters", "MDE's Oil Control search accepts Case No., Facility ID, Location, County and Address. Its instructions allow partial values but not wildcard characters. Try a shorter street value without an asterisk. Preserve the case and facility identifiers separately instead of assuming the same number identifies both.", "MDE Oil Control Case Information", MD),
                s("Include closed cases in a historic search", "The search has an Open cases only control. Check its setting before researching past cleanup or closure paperwork. A search restricted to open cases cannot answer whether a closed case exists. Record the filters and compare the location before associating an incident with the property.", "MDE case filters", MD),
                s("Move from case information to documents", "MDE's Open Records page separates searchable permit/compliance information from primary documents and links its Public Information Act route. Check available material, then request the specific Oil Control report or correspondence by case and facility ID. A result row is not a delivered closure report.", "MDE Open Records and PIA access", MD_OPEN)),
            "Please provide existing Oil Control [investigation report/cleanup report/final correspondence] for [address; county], case [number], facility [ID], [dates]. I searched including closed cases and still need [documents].",
            "Case information is not a comprehensive residential tank inventory. Partial address results require property matching; closed status alone is not a safety finding. Maryland guidance is self-service only.", "virginia-deq-petroleum-files", "conflicting-tank-documents"),
            "environmental", 0, "Open MDE Oil Control case search", "A matched case and facility ID plus the actual investigation or final correspondence.",
            "Remove wildcard characters and check Open cases only. Request named missing documents through MDE's PIA route, retaining both identifiers.",
            "the Oil Control investigation reports, cleanup reports and final correspondence for the specified case and facility",
            c("Case versus facility", "Match both numbers, county and address in the result and document.", "A shared name or partial street match is not enough."),
            c("Historic search", "Record whether closed cases were included and which attachments were obtained.", "An open-only blank does not establish absence of a historic case."),
            a("Can I search with an asterisk?", "MDE says wildcard characters are not supported; partial values are. Shorten the address without adding a wildcard."),
            a("Why is a known old case missing?", "First check the Open cases only setting, then the case number, facility ID and address variants. A failed search is not an agency no-record reply.")),
        p(e("virginia-deq-petroleum-files", "task", "virginia", "Virginia · DEQ Petroleum Tanks",
            "Virginia DEQ petroleum release records & case files",
            "Use DEQ's petroleum-release list to identify the case, then request the property file. An annual UST statistics report is a different resource.",
            "County/city, address and historic address, petroleum-release reference, approximate reported/closed dates and the specific report sought.",
            List.of(
                s("Choose the release list, not annual statistics", "DEQ's Petroleum Tanks page links a List of Petroleum Releases separately from its annual UST Public Record Report. The annual report contains program statistics; it is not the property's closure file. Use the release resource to identify the relevant location, reference and event dates.", "Virginia DEQ release-list entry point", VA),
                s("Treat missing map locations as a coverage limit", "DEQ's release-layer documentation warns that older events can lack a usable address or mapped location. Compare names, historic addresses and reported dates, not only a map radius. Preserve an unmapped or unmatched reference as a research gap rather than a conclusion that no release occurred.", "DEQ petroleum release layer limitations", "https://gisdata.deq.virginia.gov/mapper_ext/DataFactSheets/Land_Datasets/PetroleumReleases_10302020.pdf"),
                s("Follow the property-specific records route", "The Petroleum Tanks page directs people seeking information on a property or release to submit a records request. Follow that current link and confirm requester eligibility and any fees. Specify the release reference and missing reports; do not treat a listing's closed date as delivery of the underlying cleanup file.", "DEQ property / release records instructions", VA)),
            "Please provide existing [release investigation/cleanup report/closure correspondence] for [address; county or city], petroleum-release [reference], [dates]. Historic address or location details: [details]. Please advise of the applicable access requirements.",
            "The release layer can omit unmapped older events. Registration statistics and release listings do not establish present property conditions. Follow DEQ's requester-eligibility rules; self-service only.", "maryland-oil-control-case-files", "agency-record-request"),
            "environmental", 0, "Open DEQ petroleum release resources", "The release reference and case-specific reports or final correspondence, not an annual statistical report.",
            "Try historic location details and ask the listed records route about an unmapped or missing file. Confirm eligibility before submitting.",
            "the specified petroleum-release investigation, cleanup reports and closure correspondence",
            c("Correct resource", "Keep release references separate from annual UST statistics.", "A statewide report is not a search of the property's file."),
            c("Location and closure", "Compare historic location, event dates and the correspondence's stated scope.", "An absent map point or closed date is not a tank-free determination."),
            a("Does the release map include every old incident?", "DEQ warns that older events can lack a usable mapped location. A blank map cannot close the historical-record question.")),
        p(e("pennsylvania-dep-tank-files", "task", "pennsylvania", "Pennsylvania · DEP",
            "PA DEP tank records: eFACTS limits & file review",
            "Home heating-oil tanks are excluded from DEP's registered-tank list. Use that distinction to choose a regulated-facility search or a focused environmental file review.",
            "Municipality and county, address, DEP Site ID, tank-program Other ID or case reference where known, and document dates.",
            List.of(
                s("Check whether the tank list can answer your question", "DEP expressly excludes home heating-oil tanks from its registered-storage-tank lists. Active and inactive tank searches concern regulated facilities. Do not use an empty facility search to conclude that a house never used oil heat; first decide whether the target is a regulated tank or a known environmental file.", "PA DEP storage-tank data and exclusions", PA),
                s("Keep Site ID and tank-program identifiers separate", "For an applicable regulated facility, DEP distinguishes the department-wide Site ID from the tank program's Other ID and tank sequence number. Inactive searches can identify removed or permanently closed tanks. Retain those identifiers with the status, but do not mistake them for the closure report itself.", "DEP search fields and inactive tanks", PA),
                s("Use the assessment tool or informal file review", "DEP recommends its Environmental Site Assessment Search Tool or informal file review for more detailed information. Select the correct regional office and request existing reports, sample results or correspondence by reference. Some files are in older media. The agency says it does not conduct research or create reports for requesters.", "DEP environmental records and informal review", PA_REVIEW)),
            "Please provide access to existing [environmental investigation/sample results/remediation correspondence] for [address; municipality; county], DEP Site ID [number], Other ID/case [number], [dates]. Please identify the regional file holder and any older-media records.",
            "This is not a home-oil-tank registration lookup: those tanks are excluded from the published list. Local permits and private contractor documents need separate research. Self-service only.", "maryland-oil-control-case-files", "records-or-tank-sweep"),
            "registration environmental", 0, "Read DEP tank-search scope", "An applicable facility/case reference and the requested existing file—not a home-tank clearance from eFACTS.",
            "For a residential heating-oil tank, stop treating the registration list as exhaustive. Identify local work records and any separate environmental case.",
            "the existing environmental investigation reports, sample results and remediation correspondence for the specified DEP site or case",
            c("Program applicability", "Identify residential heating use versus the regulated facility represented by the list.", "A residential no-match cannot establish absence of a tank."),
            c("File identifiers", "Compare Site ID, Other ID, tank sequence and dates across the records.", "Request the report behind the status, or the older media noted in the file review."),
            a("Why is my home heating-oil tank not in eFACTS tank results?", "DEP says home heating-oil tanks are not part of the registered-tank list. The absence is a program limit, not proof about the property.")),
        p(e("washington-ecology-cleanup-files", "task", "washington", "Washington · Department of Ecology",
            "Washington Ecology cleanup search: oil tank case documents",
            "Separate Ecology cleanup-site records from regulated UST registration. Retrieve the site's reports and check PLIA or local Fire records when the file points elsewhere.",
            "Property address, county, Cleanup Site ID (CSID), Facility Site ID (FSID), report title and approximate dates.",
            List.of(
                s("Select cleanup sites or regulated tanks deliberately", "Ecology's Cleanup and Tank Search separates cleanup sites, No Further Action sites and regulated underground tank facilities. Use the cleanup search for a known release and the regulated-tank search only for its applicable facility scope. Neither is a complete register of small residential heating-oil tanks.", "Ecology Cleanup and Tank Search", ECOLOGY),
                s("Open the site documents and preserve both IDs", "For a candidate cleanup site, compare its address and retain the Cleanup Site ID and Facility Site ID. Open the actual documents rather than relying only on the status or site report. Record the document issuer: a PLIA opinion in an Ecology file still has its own issuer, program and scope.", "Ecology cleanup site search", ECOLOGY + "reports/cleanup/all"),
                s("Pursue the missing file with its actual holder", "Use the site's document and contact instructions for files not available electronically. When the known correspondence names PLIA, check PLIA's separate public-project and records route. For the removal permit or decommissioning report, use the correct city or unincorporated-county holder, not the cleanup database alone.", "Ecology search help and record boundaries", ECOLOGY)),
            "Please provide existing [cleanup report/final correspondence/attachment] for [address], Cleanup Site ID [number], Facility Site ID [number], [dates]. Known issuer/program: [Ecology/PLIA/other]. Online documents located: [list]; missing: [list].",
            "Cleanup status is not proof that every tank was removed or that all future land uses are unrestricted. Ecology, PLIA and local Fire files are separate. Washington guidance is self-service only.", "washington-plia-project-files", "seattle-residential-tank-records", "king-county-wa"),
            "environmental", 0, "Open Ecology Cleanup and Tank Search", "The correct CSID/FSID and actual case documents, with each issuer and program recorded.",
            "Check the document holder. A PLIA letter, Ecology case and local decommissioning report can require separate routes.",
            "the cleanup reports, final correspondence and named attachments for the specified Ecology CSID and FSID",
            c("Two identifiers", "Match Cleanup Site ID, Facility Site ID, address and event dates.", "A facility reference alone does not identify the complete cleanup file."),
            c("Issuer and scope", "Read who issued the opinion and any conditions or land-use limits.", "Do not relabel a PLIA opinion as an Ecology approval or a removal certificate."),
            a("Is Ecology's regulated UST search a residential oil-tank census?", "No. Its registration search covers regulated facilities; cleanup-site searches answer a separate reported-contamination question.")),
        p(e("washington-plia-project-files", "task", "washington", "Washington · PLIA",
            "PLIA public project search: heating oil cleanup letters",
            "Look for PLIA project files and written opinions, then request missing material. Insurance, assistance and local decommissioning records are different things.",
            "Property address and historic name, PLIA project/reference number, program name, opinion-letter date and the missing report.",
            List.of(
                s("Open the public project search, not a new application", "PLIA's home page directs file seekers to its Public Project Search. Use that public route to look for an existing project before starting a records request. Account login, technical-assistance applications and financial-help applications are different workflows; do not enroll simply to research a historic file.", "PLIA public-project search entry point", "https://plia.wa.gov/"),
                s("Separate the written opinion from program participation", "PLIA describes technical assistance that can include written opinions for qualifying petroleum cleanups. Keep the program and issue date with the actual letter. Insurance registration, a grant application or a technical-assistance reference does not by itself establish that cleanup was completed or a tank removed.", "PLIA Technical Assistance Program", "https://plia.wa.gov/technical-assistance-program/"),
                s("Request a specific missing PLIA record", "PLIA's public-record page recommends checking project search first and accepts a detailed written records request through its current channels. Cite the project and missing document. Its initial response may provide records, an estimated delivery date or ask for clarification; an acknowledgment is not a finished investigation.", "PLIA records requests", PLIA)),
            "Please provide existing [PLIA opinion letter/technical report/attachment] for [address], project [reference], program [name], [dates]. The public search returned [result]. This is a request for existing records, not a new assistance or insurance application.",
            "PLIA's public-project portal may need JavaScript and can be unavailable. A portal error is not no records. PLIA opinions and local decommissioning certificates are not interchangeable; self-service only.", "washington-ecology-cleanup-files", "bellevue-wa", "tacoma-wa"),
            "environmental", 0, "Open PLIA public-search entry point", "The existing project-matched opinion letter and reports, not an application or insurance reference.",
            "If public search fails or omits a file, use PLIA's records instructions with the project number. Obtain local work records separately.",
            "the PLIA written opinion, supporting technical reports and named attachments for the specified project",
            c("Project and program", "Match property, PLIA project, program and letter date.", "An insurance or assistance reference is not a cleanup opinion."),
            c("Written opinion", "Read issuer, scope, conditions and reports relied upon.", "Request a missing report; local removal evidence remains a separate question."),
            a("Should I apply for assistance to get old documents?", "No. PLIA provides a public-project search and a separate records-request route for existing files."),
            a("What if the project portal displays an error?", "Treat that as access failure, not a property finding. Use the official records instructions and preserve any known project reference.")),
        p(e("nysdec-pbs-registration-records", "task", "new-york", "New York · NYSDEC PBS",
            "NYSDEC PBS database: registration & tank record search",
            "Search bulk-storage registration by facility ID or address, then distinguish it from a spill case and local small-tank completion paperwork.",
            "Facility identification number, address and municipality, tank number/capacity/use, county and any known spill reference.",
            List.of(
                s("Use the bulk-storage database, not a renewal form", "DEC's PBS FAQ directs users to its online Bulk Storage Database to check registration information by facility identification number or address. Follow that lookup link for historic research. The online registration/renewal application is a different action and is not needed merely to view an existing facility record.", "DEC PBS lookup instructions", PBS),
                s("Check small-tank and delegated-county limits", "DEC's homeowner guide explains that most home heating-oil tanks are outside its PBS rules and points Nassau, Suffolk and Westchester residents to county Health agencies. A blank state registration search does not complete a county or municipal search. Record the tank's use and known capacity without deciding applicability from a no-match.", "DEC homeowner guide and county delegation", DEC_HOME),
                s("Request registration documents separately from spills", "For DEC-held registration or closure paperwork not online, follow DEC's records-access instructions and cite the facility and tank references. A release has a separate spill number and document trail. Keep registration status, environmental correspondence and local completion records in distinct evidence lines.", "NYSDEC records access", DEC_REQUEST)),
            "Please provide existing PBS [registration/tank closure paperwork/correspondence] for [address; municipality], facility [ID], tank [number], [dates]. Separate known spill reference: [number]. Please identify any delegated county custody.",
            "PBS is not a complete home heating-oil tank inventory. A registration status is not the municipal final inspection, spill report or present-condition finding. NJ/NY assistance remains optional and coverage-reviewed.", "nysdec-spill-documents", "nassau-ny", "suffolk-ny", "westchester-ny"),
            "registration", 0, "Open DEC PBS database instructions", "The facility/tank registration reference and actual paperwork, separate from the spill and local completion file.",
            "Check tank/program scope and delegated county custody. Do not use a state no-match to clear a small residential tank.",
            "the PBS registration, tank closure paperwork and related correspondence for the specified facility and tank",
            c("Facility and tank", "Compare facility ID, tank number, use, capacity and location.", "A similarly named facility or different tank is not a match."),
            c("Different evidence lanes", "Keep PBS, spill and county/municipal completion documents separate.", "A registration status cannot replace the underlying work or cleanup report."),
            a("Is the PBS number the same as the spill number?", "No. Preserve each reference and use the corresponding record system; they identify different record families."),
            a("Why does a small home tank have no state PBS record?", "DEC describes residential heating-oil scope limits and delegated county programs. Check the relevant county and local work-file route.")),

        p(e("boston-ma", "area", "massachusetts", "City of Boston · Massachusetts",
            "Boston oil tank removal records & Fire permits",
            "Target Fire Prevention's Special Hazards tank file, not just a building history. Use Boston's current JustFOIA route for missing historic documents.",
            "Boston address and historic street numbering, Fire permit reference, work type, removal or abandonment year and contractor if known.",
            List.of(
                s("Identify the Special Hazards tank record", "Boston Fire Prevention groups tank removal, installation/modification and temporary out-of-service documents under Special Hazards. Identify which action the historic paperwork names. A burner change, out-of-service application and tank removal are not equivalent, and an application does not prove the work was completed.", "Boston Fire Special Hazards tank categories", BOSTON),
                s("Request the existing Fire file in JustFOIA", "Boston's Public Records page says the City transitioned from GovQA to JustFOIA. Use the current request link and identify Fire Prevention/Special Hazards, the address, dates and permit. Keep the tracking/security reference private. Do not file a new removal application merely to retrieve the old file.", "Boston current public-records portal", BOSTON_RECORDS),
                s("Follow any environmental reference separately", "If the Fire file cites an oil release or RTN, use MassDEP's contaminated-property lookup and document viewer for that case. Match the incident and property before combining records. A Fire work record and a state cleanup file have different purposes; keep each issuer and missing attachment visible.", "MassDEP environmental files", MASS)),
            "Please provide the existing Fire Prevention Special Hazards [tank removal/abandonment/out-of-service] permit, inspection result and retained attachments for [Boston address], [dates], permit [number]. This is historic file research, not a new work application.",
            "Boston Fire, Inspectional Services and MassDEP hold different record sets. A permit application is not completion evidence. Self-service guidance only; no Massachusetts assisted intake.", "massdep-oil-release-files", "missing-removal-records"),
            "completion", 1, "Open Boston public-record instructions", "The existing Special Hazards permit and any retained inspection/completion attachments.",
            "Name the Fire-held tank category in JustFOIA. Check MassDEP separately only for a release/RTN reference.",
            "the Fire Prevention Special Hazards tank permit, inspection result and completion attachments",
            c("Tank action", "Compare removal, abandonment, temporary out-of-service and burner-work wording.", "A burner permit or application alone cannot prove removal."),
            c("Portal and delivery", "Keep the current request reference and actual attachments received.", "GovQA migration or an acknowledgment is not proof of a missing or delivered file."),
            a("Should I use the old GovQA request link?", "Boston's current records page directs requests to JustFOIA. Follow the City's current link, keeping any older reference available.")),
        p(e("newton-ma", "area", "massachusetts", "City of Newton · Massachusetts",
            "Newton oil tank records: Fire permit & inspection file",
            "Newton's fuel-tank/oil-burner category combines several work types. Ask Fire's record holder for the tank-specific permit and inspection attachments.",
            "Newton address, parcel if known, tank permit number, approximate work dates, work type and any NewGov reference.",
            List.of(
                s("Separate the tank from the burner category", "Newton Fire lists Fuel Tank/Oil Burner Installation/Removal in its online NewGov permit categories. Read the underlying work description rather than interpreting the category label as a removal. A new burner, tank installation and removal can lead to different evidence even when shown under one heading.", "Newton Fire forms and online categories", NEWTON),
                s("Request Fire-held historic documents", "Newton's public-record page lists a Fire Department records officer and distinguishes municipal from police requests. Use its current municipal route or listed Fire record contact, with dates and the permit number. An online permit application is not a request to retrieve a historic Fire file.", "Newton municipal records and Fire custodian", NEWTON_RECORDS),
                s("Keep retention and environmental gaps explicit", "Ask which permit, inspection and supporting records remain available for the work period. Do not assume current online permit coverage proves older material is digitized or retained. If the file names an RTN or spill, retrieve that MassDEP case separately and preserve any missing laboratory or disposal attachments.", "MassDEP case documents and local-file distinction", MASS)),
            "Please provide existing Fire Department fuel/oil tank [installation/removal] permits, inspection results and retained completion attachments for [Newton address], [dates], reference [number]. Please distinguish tank work from burner-only work and identify any historic-file limit.",
            "Current NewGov categories do not establish historical online coverage or perpetual retention. No record is not proof of no tank. Massachusetts guidance is self-service only.", "massdep-oil-release-files", "wellesley-ma", "conflicting-tank-documents"),
            "completion", 1, "Open Newton public-record instructions", "The tank-specific Fire work description, inspection result and retained supporting file.",
            "Specify tank work rather than burner-only work. Ask about older-file/retention limits instead of treating a portal blank as a property finding.",
            "the Fire-held tank-specific permit, inspection results and retained completion attachments, excluding burner-only work",
            c("Combined permit category", "Read whether the described action concerns the tank or only the burner.", "A category heading alone does not show removal."),
            c("Historic file scope", "Keep dates, records searched and any stated retention limit.", "Do not describe an unavailable older attachment as proof work did not occur."),
            a("Does Fuel Tank/Oil Burner Installation/Removal prove removal?", "No. Read the specific work description and completion/inspection record; the combined category covers different actions.")),
        p(e("wellesley-ma", "area", "massachusetts", "Town of Wellesley · Massachusetts",
            "Wellesley oil tank removal records & disposal receipt",
            "Look for the Fire removal/inspection file and the signed disposal receipt identified in Town guidance. A permit status is not that receipt.",
            "Wellesley address, tank capacity and above/underground type, approximate removal date, contractor and permit reference.",
            List.of(
                s("Identify the disposal receipt behind the permit", "Wellesley's tank-removal instructions identify a signed disposal receipt, Form FP 291, forwarded to Fire after removal. That is a specific supporting document to request alongside the permit and inspection. The published instructions do not prove a receipt was filed for every historic removal.", "Wellesley tank-removal documentation", WELLESLEY),
                s("Use the Town's records-access route", "Wellesley's public-record page identifies Records Access Officers and current request guidance. Ask for the existing Fire-held tank file with the removal date and permit reference. Name the signed disposal receipt rather than requesting a new removal permit, a blank form or a general opinion on the property.", "Wellesley Public Records", WELLESLEY_RECORDS),
                s("Compare underground and aboveground evidence", "The Town's instructions distinguish underground removal observation from verification after aboveground work. Compare the tank type, disposal destination and signatures actually shown in returned papers. Where an RTN or cleanup report is cited, use MassDEP separately; a disposal receipt alone does not resolve soil conditions.", "Wellesley inspection and tank-type distinctions", WELLESLEY)),
            "Please provide the existing Fire oil tank removal permit, inspection/verification record and signed disposal receipt (FP 291), for [address], [aboveground/underground; capacity], removed [dates], permit [number]. Please identify which attachments are held or missing.",
            "A receipt concerns the tank and disposal stated on it, not every tank or all contamination. Published work instructions are not proof of a property's completed file. Self-service only.", "massdep-oil-release-files", "newton-ma", "missing-removal-records"),
            "completion", 1, "Open Wellesley records-access instructions", "The Fire permit/inspection file plus the signed FP 291 disposal receipt, where retained.",
            "Request the named signed receipt and inspection attachment. Record a missing receipt separately from a missing permit.",
            "the Fire tank removal permit, inspection or verification record and signed FP 291 disposal receipt",
            c("Signed receipt", "Compare tank type/capacity, removal date, disposal destination and signatures.", "A blank FP 291 form is not the property's disposal receipt."),
            c("Inspection versus disposal", "Keep the Fire observation/verification and disposal evidence distinct.", "A permit or receipt alone does not answer every completion or soil question."),
            a("What extra document should I name in Wellesley?", "The Town identifies a signed disposal receipt, FP 291, returned to Fire. Ask for the existing receipt and associated inspection, not a blank application.")),
        p(e("scarsdale-ny", "area", "new-york", "Scarsdale · Westchester County",
            "Scarsdale oil tank permit records & property-file attachments",
            "Use the Village-linked property information system, then request the tank permit and missing attachments. County PBS and DEC spill files remain separate.",
            "Village property address, section/block/lot, oil-tank or mechanical permit number, approximate work year and document titles.",
            List.of(
                s("Start with the Village-linked property information", "Scarsdale's permit-application page links its Property Information system separately from the Oil Tank Application. Follow the property link for historic research and compare parcel and permit references. Do not submit the new oil-tank application just to learn what documents are already in the property file.", "Scarsdale official property and tank entry points", SCARSDALE),
                s("Request the underlying Building attachments", "Use the Village's online FOIL instructions for an existing oil-tank permit, work description, inspection record and retained completion attachments. Include the permit and parcel references from the property search. A status or note is not the underlying contractor report, disposal record or agency inspection.", "Scarsdale FOIL records request", SCARSDALE_FOIL),
                s("Preserve county and spill references separately", "DEC's homeowner guide points Westchester residents to the county Health program for delegated tank questions. Check a referenced county PBS file separately from a DEC spill case. Compare the parcel, tank description and dates before joining their documents; a Village response does not complete either agency's search.", "DEC delegated-county and small-tank guidance", DEC_HOME)),
            "Please provide the existing Building oil tank permit, inspection result and retained removal/abandonment completion attachments for [Scarsdale address; section/block/lot], permit [number], [dates]. The property system shows [reference], but [named attachments] remain missing.",
            "Property-system coverage and attachment access can vary. A completed status needs its supporting scope checked. Village, Westchester Health and DEC documents are different records; optional NJ/NY assistance is coverage-reviewed.", "westchester-ny", "nysdec-pbs-registration-records", "nysdec-spill-documents"),
            "completion", 0, "Open Scarsdale property-record entry point", "The parcel-matched tank permit and actual Building inspection/completion attachments.",
            "Use Village FOIL for named attachments, then county Health or DEC for their separately identified files.",
            "the Building oil tank permit, inspection results and retained removal or abandonment completion attachments",
            c("Property entry", "Compare section/block/lot, permit, action and dates.", "A permit note or completed label is not all the supporting evidence."),
            c("Attachments and issuers", "List Village, contractor, county and DEC documents independently.", "One custodian's response cannot clear the other file searches."),
            a("Should I file the Oil Tank Application for old paperwork?", "No. Use the property information link for historic references and FOIL for existing attachments.")),
        p(e("greenwich-ct", "area", "connecticut", "Town of Greenwich · Connecticut",
            "Greenwich oil tank records: Fire FOIA & Building permits",
            "Greenwich Fire publishes an oil-tank information request route. Keep that file separate from the Building removal/abandonment permit and DEEP spill documents.",
            "Greenwich property address, parcel if known, approximate work date, removal versus abandonment method and Fire/Building permit references.",
            List.of(
                s("Use Fire's oil-tank information route", "The Fire Marshal's Division page links a FOIA request for oil tank information under Underground Tank Removal. Use that existing-record route and describe the property, work year and needed report. Do not confuse a request for the historic Fire file with an appointment to approve new tank work.", "Greenwich Fire oil-tank FOIA entry point", GREENWICH),
                s("Check the separate Building work description", "Greenwich Building publishes removal/abandonment permit instructions that distinguish work methods. Its resident property resources link City Squared permit history. Compare the actual method and permit reference; a Building application, Fire report and completed removal are different evidence stages.", "Greenwich permit history and property resources", "https://www.greenwichct.gov/3173/Property-Information-Resources"),
                s("Name the report's missing attachments", "Greenwich Fire's tank FAQs describe a removal-company report with soil-analysis and location-drawing attachments. Ask which existing parts are retained for the subject work, without assuming they were filed. If the documents cite a spill or DEEP case, pursue that state file separately and keep its closure scope distinct.", "Greenwich Fire tank-record details", "https://www.greenwichct.gov/m/faq?cat=47")),
            "Please provide existing Fire oil tank removal/abandonment reports and retained laboratory/site-drawing attachments for [address], [dates], reference [number]. Please distinguish the Fire file from the associated Building permit and identify any missing record category.",
            "Published filing instructions do not establish that a report exists for this property. Fire, Building and DEEP custody differ. Connecticut guidance is self-service only.", "connecticut-deep-spill-documents", "stamford-ct", "records-or-tank-sweep"),
            "completion", 0, "Open Greenwich oil-tank records instructions", "The Fire report and retained lab/location attachments, with the Building method/permit matched separately.",
            "Ask for the precise Fire-held attachment and inspect the Building work method. Use DEEP for the separately identified environmental case.",
            "the Fire oil tank removal or abandonment report and retained laboratory analysis and tank-location drawing",
            c("Work method", "Compare removal, temporary out-of-service and abandonment wording.", "Do not turn an ambiguous method into proof the tank left the property."),
            c("Report attachments", "List the lab analysis and tank-location drawing actually received.", "The filing instructions are not the property's report or its missing attachments."),
            a("Why check both Fire and Building?", "Greenwich supplies an oil-tank information route through Fire and a separate Building permit-history route. Match the work and documents without treating one office's reply as both searches.")),
        p(e("westport-ct", "area", "connecticut", "Town of Westport · Connecticut",
            "Westport oil tank records: tank & spill document archive",
            "Use Westport's dedicated COs, Tank and Spill Document Archive for historic tank papers, then its separate permit/inspection search for the mechanical file.",
            "Westport property address, older address if known, mechanical permit reference, tank work date and the missing report or attachment.",
            List.of(
                s("Choose the tank archive, not only permit search", "Westport Building directs users seeking oil-tank information and older certificates to its COs, Tank and Spill Document Archive, searchable by address. Follow that named archive link on the official page. A blank result in the general Building permit portal does not complete the archive search.", "Westport Building archive instructions", WESTPORT),
                s("Match the mechanical permit and inspections", "The same department supplies a separate online permit/inspection route. Westport's residential tank guidance identifies a mechanical permit for removals and links oil-tank removal documents. Compare permit reference, work type, dates and the archive's actual files; an inspection index is not every underlying report.", "Westport residential tank documents", "https://www.westportct.gov/government/departments-a-z/building-department/residential-oil-tank-guidelines"),
                s("Ask Building for a named archive gap", "When the archive has a listing without a needed file, record the address spelling and document title and use Building's official contact/request instructions. State which archive and permit searches you checked. For a DEEP spill reference, use the state document route separately rather than assuming every state attachment is held by the Town.", "Westport Building record contact and sources", WESTPORT)),
            "Please provide existing tank removal/abandonment documents and retained [inspection/report/attachment] for [Westport address], [dates], mechanical permit [number]. I checked the COs, Tank and Spill Document Archive and permit search; the missing material is [description].",
            "The dedicated archive and current permit portal have different scopes. No archive result does not establish no tank. State environmental documents require their own search; self-service only.", "connecticut-deep-spill-documents", "wilton-ct", "missing-removal-records"),
            "completion", 0, "Open Westport tank-archive instructions", "The address-matched historic tank documents plus the mechanical permit/inspection references.",
            "Check both the named archive and permit portal. Ask Building for the specific missing file, then DEEP for a cited state case.",
            "the archived tank removal or abandonment documents, mechanical permit and associated inspection or completion attachments",
            c("Two searches", "Keep archive and permit searches as separate source checks.", "A general permit no-match does not settle the tank archive."),
            c("Actual file", "Compare work method, date and document title with the downloaded file.", "A listing without a PDF remains an attachment gap."),
            a("Where are Westport's older tank documents?", "Building directs oil-tank and older certificate research to its COs, Tank and Spill Document Archive. That is separate from current permit and inspection search.")),
        p(e("stamford-ct", "area", "connecticut", "City of Stamford · Connecticut",
            "Stamford oil tank removal records: Fire Marshal FOI",
            "Stamford Fire keeps removal documents when supplied, not a complete tank register. Ask for the retained file and preserve what the owner or contractor still holds.",
            "Stamford address, approximate removal date, contractor, permit or Fire reference, and the specific reports or laboratory attachments needed.",
            List.of(
                s("Understand the voluntary-file coverage limit", "Stamford Fire's environmental-record guidance says owners retain tank permits, documents and lab results, while Fire keeps removal records when they are provided. Not every tank has a removal record there. An empty Fire search therefore does not prove a tank was never removed or was never present.", "Stamford Fire environmental-record scope", STAMFORD),
                s("Use the Fire Marshal FOI request", "The official page links a Fire Marshal records-request form and asks for a detailed description. Specify the address, approximate tank-work dates and retained report/attachments sought. Check the current copy-fee and release instructions before submitting; this page does not send a request or arrange pickup.", "Stamford Fire FOI instructions and form", STAMFORD),
                s("Keep owner documents and DEEP separate", "Use an owner- or contractor-supplied report as its own evidence source and compare its identifiers against any Fire documents returned. For a known leak or state case reference, search DEEP reports independently. Do not relabel an invoice as a Fire completion certificate or a state database blank as a whole-property clearance.", "DEEP environmental-document routes", CT)),
            "Please provide any retained Fire Marshal underground oil tank removal reports, permits and laboratory attachments supplied for [Stamford address], work [dates], contractor/reference [details]. Please state the record categories searched if none are located.",
            "Fire explicitly warns that not every tank has a removal record. Private owner/contractor documents and state files may be separate. Self-service only; fees or access conditions follow current agency instructions.", "greenwich-ct", "connecticut-deep-spill-documents", "seller-says-tank-removed"),
            "completion", 1, "Open Stamford Fire records instructions", "Any retained owner/contractor-supplied Fire removal file and its actual lab attachments.",
            "Preserve the limited Fire search scope. Seek the specific owner/contractor report and any separately identified DEEP case.",
            "the retained Fire Marshal tank removal reports, permits and laboratory attachments supplied for the property",
            c("Record provenance", "Identify owner/contractor documents versus the Fire-held copy.", "An invoice is not an agency inspection or completion finding."),
            c("No-record reply scope", "Keep the categories and work dates Fire searched.", "Stamford's incomplete supplied-file coverage prevents a tank-free inference."),
            a("Does Stamford Fire have every removal record?", "No. Its guidance says it keeps records when provided and warns that not all tanks have a removal record. Retain that limit with any response.")),
        p(e("wilton-ct", "area", "connecticut", "Town of Wilton · Connecticut",
            "Wilton oil tank records: historical files through October 2018",
            "Wilton separates historic oil-tank information formerly held by Fire from newer permitting. Use the stated cutoff before choosing the record system.",
            "Wilton property address, tank work date, permit/reference number and whether the event was before or after October 2018.",
            List.of(
                s("Use the historical oil-tank link for the stated period", "Wilton's historical-records page identifies Oil Tank information through October 2018 previously maintained by the Fire Marshal. Follow that specific link for the older work period, not just a new permit application. Compare address and document dates and retain the historic collection's cutoff.", "Wilton historic oil-tank collection instructions", WILTON),
                s("Do not substitute another archive's cutoff", "The same page lists historical Building and Health records and newer online permitting separately. Their periods and record types are not necessarily the oil-tank collection's. For later tank work, ask the relevant Town record holder where that file is maintained rather than extending the October 2018 collection's scope.", "Wilton archive and permitting distinctions", WILTON),
                s("Request the precise historic or later-file gap", "If a document cannot be opened or the year is outside the stated tank collection, use the Town department contact on its official instructions to identify the current holder and request the existing report. A blocked archive is an access failure. Follow any known DEEP case in the separate state document system.", "Wilton record routing and contacts", WILTON)),
            "Please help locate the existing tank [removal/abandonment/report] file for [Wilton address], work [dates], reference [number]. I checked the historical Oil Tank collection through October 2018; [document or later-period record] remains missing. Please identify the current custodian.",
            "October 2018 is the published historical oil-tank collection boundary, not proof all older work was filed or that newer records are absent. An archive access error is not no records. Self-service only.", "westport-ct", "connecticut-deep-spill-documents", "agency-record-request"),
            "completion", 0, "Open Wilton historical-record instructions", "The historic oil-tank document for the stated collection period, or a verified referral for later work.",
            "Record the October 2018 boundary and ask for the later-file holder when needed. Do not apply the Building/Health archive dates to the tank collection.",
            "the existing historical oil tank removal or abandonment report and attachments, or the identified later-period tank file",
            c("Collection and dates", "Compare the tank-work year with the oil-tank collection through October 2018.", "Another archive's date range is not the tank archive's coverage."),
            c("Access versus absence", "Record a failed download, out-of-period search or custodian referral separately.", "None alone establishes that no file or tank exists."),
            a("Does the historical tank archive cover new work?", "Wilton describes this collection as through October 2018. For later work, confirm the current Town holder rather than assuming either coverage or absence."))
    );
    public static final List<Entry> AREAS = PROFILES.stream().map(Profile::entry).filter(e -> e.kind().equals("area")).toList();
    public static final List<Entry> TASKS = PROFILES.stream().map(Profile::entry).filter(e -> e.kind().equals("task")).toList();
    public static Profile profile(String slug) { return PROFILES.stream().filter(p -> p.entry().slug().equals(slug)).findFirst().orElse(null); }
}
