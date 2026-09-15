package owner.buriedoiltank.data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/** Public research guidance, never customer evidence or proof of agency fulfillment. */
public final class ResearchCatalog {
    private ResearchCatalog() {}
    public static final LocalDate REVIEWED = LocalDate.of(2026, 9, 16);
    public static final String HUB = "/research-areas/";
    public record Step(String title, String body, String sourceLabel, String sourceUrl) {}
    public record Entry(String slug, String kind, String state, String locality, String title,
                        String summary, String identifiers, List<Step> steps, String request,
                        String boundary, String question, List<String> related) {
        public String path() { return "/" + (kind.equals("area") ? "research-areas" : "record-help") + "/" + slug + "/"; }
        public String id() { return "research:" + slug; }
    }
    private static Step step(String title, String body, String label, String url) {
        return new Step(title, body, label, url);
    }
    private static final String DEP = "https://dep.nj.gov/opra/";
    private static final String MINER = "https://njems.nj.gov/DataMiner";
    private static final String NYC = "https://www.nyc.gov/site/buildings/dob/find-building-data.page";
    private static final String FDNY = "https://www.nyc.gov/site/fdny/about/resources/record-requests/records-request.page";
    private static final String NASSAU = "https://www.nassaucountyny.gov/2059/Homeowner-Small-Fuel-Oil-Tank-Removals";
    private static final String MAPLEWOOD = "https://www.maplewoodnj.gov/government/township-clerk/applications-and-forms/opra-open-public-records-act-request";

    private static final List<Entry> ORIGINAL_AREAS = List.of(
        new Entry("montclair-nj", "area", "new-jersey", "Montclair · Essex County",
            "Montclair oil tank removal and property records",
            "Start with Montclair's property portal, then separate a municipal permit search from an NJDEP environmental file search.",
            "Street address, block and lot, any prior address, approximate work dates, and any permit or NJDEP case number.",
            List.of(
                step("Search the property file in SDL", "Montclair directs property research to its Properties Information Portal. A free account may be needed. Search the address and parcel, then note permit and inspection references. Keep a dated search record even if the result is empty; a portal result is not the complete historic file.", "Montclair Properties Information Portal", "https://www.montclairnjusa.org/Government/Departments/Building-Office/Montclair-Properties-Information-Portal"),
                step("Ask the Township for the missing documents", "Use Montclair's OPRA entry point for identifiable municipal records. Our suggested sequence is to request the tank-related permit, inspection result, and completion document behind a portal entry, with a bounded date range. Record what was requested separately from what the Township actually returns.", "Montclair OPRA requests", "https://www.montclairnjusa.org/Site-Footer/Footer-Widgets/Useful-Links/File-an-OPRA-Request"),
                step("Check the state environmental file separately", "Use NJDEP records routes when the paperwork names a spill, remediation case, or department identifier. The municipal file and environmental file answer different questions. Compare parcel, tank location, dates, and case number before joining them into one history.", "NJDEP records access", DEP)),
            "Please provide existing tank installation, removal or abandonment permits, inspection results, and completion records for [address; block/lot], concerning [approximate dates or permit number]. Please identify any referral needed for records held elsewhere.",
            "A construction record is not proof that every tank was found, and an NJDEP search without a match is not a tank-free finding.", "find_records", List.of("missing-removal-records", "seller-says-tank-removed")),
        new Entry("maplewood-nj", "area", "new-jersey", "Maplewood · Essex County",
            "Maplewood oil tank removal certificate research",
            "Maplewood specifically lists oil tank removal certificates among requestable records. Locate the certificate and compare it with the permit history.",
            "Property address or block and lot, date range, permit number if known, and the precise record type sought.",
            List.of(
                step("Check the online construction history", "The Clerk's guidance links online construction and assessment information. Use these to resolve the property and identify relevant permit references. A permit entry can narrow the request, but it does not substitute for the final inspection or certificate.", "Maplewood Clerk's records guidance", MAPLEWOOD),
                step("Request the named certificate", "Maplewood's OPRA page explicitly lists a Certificate of Oil Tank Removal, permit records, and property cards. Name the documents you need instead of asking the Clerk to answer whether a property is safe. Use the official request channel and disclose the request purpose accurately.", "Maplewood OPRA record types and request channel", MAPLEWOOD),
                step("Separate a removal certificate from environmental closure", "If returned papers reference contamination or an NJDEP case, follow that identifier into the state records route. Our comparison checks the document's issuer, subject parcel, tank description, and the action actually recorded; similarly named documents need not establish the same thing.", "NJDEP records access", DEP)),
            "Please provide the Certificate of Oil Tank Removal, associated permit, and final inspection record for [address or block/lot], [date range]. The relevant permit reference, if available, is [number].",
            "The record type being available for request does not mean a certificate exists for this property. Agency timing, access restrictions, and charges are separate from the free research beta.", "find_records", List.of("missing-removal-records", "conflicting-tank-documents")),
        new Entry("summit-nj", "area", "new-jersey", "Summit · Union County",
            "Summit oil tank permit and closure record research",
            "Summit links building permits before its non-police records request portal. Use that two-stage route to identify and retrieve the underlying file.",
            "Street address, block and lot, approximate installation or removal year, and any building permit reference.",
            List.of(
                step("Use the City's building permit link", "Summit's OPRA page links building permits in SDL alongside tax information and maps. Begin with the property and relevant work dates. Save the permit reference and the exact status displayed rather than interpreting an application as completed work.", "Summit public records and permit links", "https://www.cityofsummit.org/905/OPRA-Requests"),
                step("Use the non-police request portal", "The same City page identifies its non-police portal for requests, status, correspondence, and returned documents. Request the underlying tank-related permit and inspection record from that route. Keep the request number and acknowledgment so an open request is not mistaken for a missing file.", "Summit OPRA request routing", "https://www.cityofsummit.org/905/OPRA-Requests"),
                step("Follow environmental references, not assumptions", "Where a municipal record cites NJDEP involvement, use the department's record-access route to pursue that file. We keep the municipal work history and environmental case history in separate lines until the property and identifiers match.", "NJDEP records access", DEP)),
            "Please provide existing tank-related building permits and associated inspection/completion documents for [address; block/lot], [date range], including records associated with [known permit number].",
            "The non-police portal is a request channel, not a guarantee that every historic attachment is online. An issued permit alone does not establish completion.", "find_records", List.of("conflicting-tank-documents", "agency-record-request")),
        new Entry("wayne-nj", "area", "new-jersey", "Wayne · Passaic County",
            "Wayne oil tank permit and property file research",
            "Wayne distinguishes self-service searches, property maps, and an OPRA fallback. Research follows that sequence before escalating missing tank paperwork.",
            "Street address, parcel block and lot, prior address if applicable, date range, and any work or permit identifier.",
            List.of(
                step("Start with the Self Service Portal", "Wayne's Clerk recommends checking the Self Service Portal before filing OPRA. Search available property records and capture the displayed reference and search date. We do not assume that the portal's online coverage includes every historic tank record.", "Wayne Clerk's search and OPRA guidance", "https://waynetownship.com/township-council/clerks/open-public-records-act/"),
                step("Resolve the parcel before requesting records", "The Clerk separately links maps and the property database. Our workflow uses those identifiers to distinguish the subject lot from neighboring properties or renamed streets. Then the official OPRA form can identify specific permits and inspection documents not found online.", "Wayne property and OPRA entry points", "https://waynetownship.com/township-council/clerks/open-public-records-act/"),
                step("Keep state findings in their own evidence lane", "NJDEP DataMiner may help locate department records or identifiers. A result must be matched to the property before it is useful. If an underlying report is not available online, follow NJDEP's own access process rather than assuming the Township holds it.", "NJDEP DataMiner", MINER)),
            "Please provide existing oil tank installation, removal or abandonment permits and inspection records for [address; block/lot] during [date range]. Known reference: [permit number]. Please advise if responsive records require a separate custodian.",
            "Use the municipal records route, not a police-record request. Public database coverage and agency response periods are not a promised final delivery date.", "agency_follow_up", List.of("agency-record-request", "records-or-tank-sweep")),
        new Entry("nassau-ny", "area", "new-york", "Nassau County",
            "Nassau oil tank removal verification letter research",
            "Nassau has a small heating-oil tank closure route distinct from bulk-storage research. Choosing the right system matters more than searching every database.",
            "Property address, municipality, section/block/lot if available, tank size and use, and approximate removal or abandonment date.",
            List.of(
                step("Choose the small-tank record route", "Nassau's homeowner page links small fuel-oil tank removal and abandonment guidance. The county's small-tank system offers address-based verification-letter lookup for recorded completed work. Start there when the subject is a residential heating-oil tank, and check the system's eligibility before using it.", "Nassau homeowner small-tank guidance", NASSAU),
                step("Look for a verification letter, not a new work request", "The public closure system separates scheduling from printing a verification letter. For historic research, use the verification search. Save the letter and match its address, work type, and date. Do not submit a removal appointment merely to obtain records.", "Nassau small heating-oil tank closure system", "https://apex5.nassaucountyny.gov/ords/f?p=340:97"),
                step("Escalate an empty or ineligible search", "Nassau's Environmental Health page distinguishes small heating-oil tanks from bulk-storage facilities. Use the listed department route to clarify an unmatched historic record or a different tank category. Our next request asks what records were searched and where older material may be held.", "Nassau Environmental Health tank routes", "https://www.nassaucountyny.gov/3027/Environmental-Health")),
            "I am seeking an existing removal or abandonment verification record for [address], approximately [year], for a [known size/use] tank. The public verification search returned [result]. Please identify the appropriate record search or request route; this is not a request to schedule new tank work.",
            "An empty verification search may reflect unfiled work or coverage limits. A bulk-storage database is not a complete register of residential tanks. Neither result establishes present physical conditions.", "find_records", List.of("missing-removal-records", "seller-says-tank-removed")),
        new Entry("suffolk-ny", "area", "new-york", "Suffolk County",
            "Suffolk oil tank and Health Department record research",
            "Prepare the full tax-map identifier for Suffolk's Health Department record search, and keep town or village permits separate from county-held records.",
            "One property address, full district/section/block/lot tax-map number, municipality, specific record types, and approximate dates.",
            List.of(
                step("Resolve the complete tax-map number", "Suffolk's Health Department public-access application asks for an accurate physical address and full tax-map identification, and specifies one address per request. Gather district, section, block, and lot before drafting a request; a mailing city alone may not identify the correct file.", "Suffolk Health Department records application (2023)", "https://www.suffolkcountyny.gov/Portals/0/documentsforms/healthservices/Wastewater%20Management/Forms/APPLICATION_FOR_PUBLIC_ACCESS_TO_RECORDS_revised_2023.05.pdf"),
                step("Use the current county request entry point", "The county's Citizen Services page lists its FOIL portal. Use current county instructions to confirm routing and submission requirements. The older Health Department form is useful for identifier preparation, but should not be treated as proof that the old submission method is the only current channel.", "Suffolk Citizen Services", "https://www.suffolkcountyny.gov/services/citizen-services"),
                step("Ask separately about municipal work records", "Our research plan separates county Health Department records from permits potentially held by the property's town or village. Use the exact municipality to identify its building-record custodian. Record a referral as a referral, not as a completed search or confirmation that a tank was removed.", "Suffolk county services directory", "https://www.suffolkcountyny.gov/services/citizen-services")),
            "Please provide existing Health Department records concerning oil tank installation, removal, abandonment, or reported releases for [one address; district/section/block/lot], [date range]. Please identify a referral if the requested record category is held by another agency.",
            "This is a county research starting point, not a verified permit route for every Suffolk town and village. A county no-records response does not resolve records held by a municipality or private contractor.", "agency_follow_up", List.of("agency-record-request", "missing-removal-records")),
        new Entry("westchester-ny", "area", "new-york", "Westchester County",
            "Westchester oil tank closure file research",
            "Distinguish a historic petroleum-storage map from a current property file. Use tank and permit identifiers to pursue the documents behind a closure claim.",
            "Property address, municipality, current and former parcel numbers if known, tank capacity/use, work dates, and PBS or permit number.",
            List.of(
                step("Check the age and scope of a map hit", "Westchester's published petroleum bulk-storage map metadata describes active facilities as of June 2017. Treat that layer as a historic lead, not a live inventory of all tanks. Save the identifier and vintage; do not infer absence from a property not appearing on the map.", "Westchester PBS layer metadata", "https://giswww.westchestergov.com/arcgis/rest/services/DataHub_EnvironmentandPlanning/MapServer/165/iteminfo"),
                step("Identify the closure documents worth requesting", "County PBS work-permit instructions describe supporting closure materials, including disposal documentation and, for applicable work, sampling and closure reports. Those instructions help identify possible record categories. Ask the Health Department whether the program and file apply to this tank before treating its requirements as universal.", "Westchester PBS work-permit instructions", "https://health.westchestergov.com/images/stories/PDF/pbsworkpermitinstructions2023.pdf"),
                step("Confirm the custodian and municipality", "The Health Department directory identifies a PBS contact route. Our next step is to establish the correct record-access channel and file identifier there, while checking which local building office holds municipal permits. The county and municipal searches remain separately logged.", "Westchester Health Department program contacts", "https://health.westchestergov.com/component/content/article?Itemid=158%2F1000&id=1546")),
            "Please identify the records-access route for the PBS or tank-work file at [address; municipality; parcel], reference [number]. We seek existing permits, closure reports, inspection records, and disposal documentation for [dates], where held by your department.",
            "The public map is dated and program applicability depends on the tank. This route does not claim a county closure file exists for every residential tank, or that a map hit is a contamination finding.", "find_records", List.of("conflicting-tank-documents", "seller-says-tank-removed")),
        new Entry("new-york-city", "area", "new-york", "New York City · Five boroughs",
            "NYC oil tank records: DOB history and FDNY reports",
            "NYC tank research crosses DOB's older and newer filing systems and FDNY's tank-specific reports. One building search is not the whole record.",
            "Street address, borough, BIN and borough/block/lot if available, filing or job number, tank type, and approximate work dates.",
            List.of(
                step("Check both DOB record systems", "NYC directs building-history research to BIS and DOB NOW. BIS does not include DOB NOW filings. Search the subject building in the appropriate systems and preserve job numbers, permit status, and dates before seeking underlying documents.", "NYC DOB building-data guide", NYC),
                step("Identify the relevant FDNY report", "FDNY lists Fuel Tank Special Reports separately from building permits. Its form distinguishes existing tanks, removed or sealed tanks, leak history, and test results. Select the record category that matches the question instead of requesting an unspecified environmental clearance.", "FDNY record requests", FDNY),
                step("Confirm submission and payment before requesting", "The linked FDNY form specifies a fee and mail or in-person submission with payment; the current webpage also lists a report-specific email contact. Confirm the current accepted route with the unit before sending or paying. We flag this channel difference rather than promising an instant email report.", "FDNY fuel-tank report form (2025)", "https://www.nyc.gov/assets/fdny/downloads/pdf/about/public-records-fuel-tank-special-reports.pdf")),
            "For [address; borough; BIN/BBL], please confirm the current procedure and fee for the Fuel Tank Special Report covering [existing / removed or sealed heating-oil tanks / leak history / test results]. This is a records inquiry, not authorization for new work.",
            "A DOB permit and an FDNY tank report have different coverage. A removed-tank entry does not by itself establish that no other tank remains, and report availability is not a safety determination.", "find_records", List.of("conflicting-tank-documents", "agency-record-request"))
    );

    public static final List<Entry> AREAS = Stream.concat(ORIGINAL_AREAS.stream(), AdditionalResearchAreas.ALL.stream()).toList();
    public static long areaCount(String state) { return AREAS.stream().filter(e -> e.state().equals(state)).count(); }

    public static final List<Entry> PROBLEMS = List.of(
        new Entry("missing-removal-records", "problem", "", "Missing paperwork",
            "Oil tank removed, but no removal records?",
            "Reconstruct the document trail by issuer and date before treating a missing certificate as proof of either completed work or an unsafe property.",
            "Address and parcel, claimed removal year, contractor name, invoices, photographs, permit references, and any seller-provided documents.",
            List.of(
                step("Turn the claim into a document list", "Write down exactly what is claimed: removal, abandonment in place, conversion to gas, or environmental closure. Those are different events. Our first request targets the permit, inspection result, and completion document for the claimed work. Maplewood illustrates why local terminology matters: its Clerk names an oil tank removal certificate as a record category.", "Example: Maplewood's named record types", MAPLEWOOD),
                step("Search the appropriate local system", "A generic statewide database may not be the right starting point. Nassau provides a specific homeowner small-tank route. Use your jurisdiction's workflow and keep a source/date/result log so the eventual brief distinguishes searched-and-empty from not-yet-searched.", "Example: Nassau homeowner tank records", NASSAU),
                step("Pursue the gap without inventing a conclusion", "If the permit exists but the completion document does not, request the attachment or final inspection. If papers cite a state environmental case, pursue that separately. An unanswered request stays pending; a custodian's no-records response is limited to the files and scope actually searched.", "NJDEP access for state-held records", DEP)),
            "Please provide existing permits, final inspections, and removal or abandonment completion records for [property], concerning work reportedly performed during [dates] by [contractor if known]. Known document reference: [number].",
            "We can report the record trail and unresolved gaps. We cannot recreate a missing certificate, certify unseen work, or guarantee that an agency retained the file.", "find_records", List.of("maplewood-nj", "nassau-ny", "seller-says-tank-removed")),
        new Entry("seller-says-tank-removed", "problem", "", "Seller or listing claim",
            "How to check a seller's oil tank removal claim",
            "Treat the seller's statement as the question to investigate, then match independent documents to the parcel, tank, date, and claimed action.",
            "The exact written claim, disclosure or listing, address, alleged work date, contractor details, and any removal, abandonment, or inspection documents.",
            List.of(
                step("Separate removed from sealed or converted", "Our review starts by preserving the seller's exact wording. A switch to gas does not describe what happened to a tank. NYC's FDNY form distinguishes report categories for existing and removed or sealed tanks, illustrating why the action named in a document matters.", "FDNY tank report categories", "https://www.nyc.gov/assets/fdny/downloads/pdf/about/public-records-fuel-tank-special-reports.pdf"),
                step("Match the evidence to this property", "Compare address, parcel, issuer, dates, tank size and location where recorded. A contractor invoice records a claim about work; a permit records authorization; neither should silently become an agency completion finding. Retrieve the underlying local record, such as Nassau's verification route where applicable.", "Nassau homeowner record route", NASSAU),
                step("Deliver a claim-by-claim result", "The brief records each claim as supported by a named document, contradicted by a specific record, or unresolved. If a state case is mentioned, check its documents separately. Questions for an attorney, inspector, or environmental professional are flagged rather than answered outside research scope.", "NJDEP state-record access", DEP)),
            "Please provide existing records documenting [claimed removal or abandonment] at [property] during [dates], including the final inspection or completion record associated with [permit].",
            "Research is not a seller credibility score, legal opinion, or promise that a transaction should proceed. Do not delay urgent professional assessment while waiting for records.", "verify_claim", List.of("montclair-nj", "nassau-ny", "records-or-tank-sweep")),
        new Entry("conflicting-tank-documents", "problem", "", "Document interpretation",
            "Conflicting oil tank permits, invoices, and closure papers",
            "Build a dated evidence table when the permit, contractor paperwork, and agency record do not seem to tell the same story.",
            "All versions of the documents, their issuer and date, permit or case numbers, property identifiers, and the specific passages that appear inconsistent.",
            List.of(
                step("Separate authorization, work, and completion", "Our comparison gives each document a limited role. An application, issued permit, inspection, invoice, and agency response are not interchangeable milestones. In NYC, check both BIS and DOB NOW before treating one system's status as the entire building history.", "NYC building-history systems", NYC),
                step("Check whether the papers concern the same tank", "Compare parcel, capacity, location, work date, and reference numbers. Westchester's PBS instructions illustrate the range of supporting closure materials that may sit behind a file. The existence of an instruction list is not evidence those documents were submitted in your case.", "Westchester closure-document guidance", "https://health.westchestergov.com/images/stories/PDF/pbsworkpermitinstructions2023.pdf"),
                step("Request the document that resolves the mismatch", "If the conflict is issued versus final, seek the final inspection. If it is removal versus abandonment, seek the work description and completion record. If it is municipal completion versus an environmental case, request the state file separately. We preserve unresolved contradictions rather than selecting the most reassuring page.", "NJDEP records access", DEP)),
            "Please provide the final inspection and completion documents associated with [permit/case number] at [property]. We already hold [document and date] and seek the underlying record of [specific missing action].",
            "A research comparison does not determine compliance, contamination, or contractual rights. Conflicting documents may require clarification by the issuing agency or a qualified professional.", "interpret_documents", List.of("summit-nj", "westchester-ny", "new-york-city")),
        new Entry("agency-record-request", "problem", "", "Agency routing",
            "Request missing oil tank records from the right agency",
            "A useful request names existing documents, the exact property, and the responsible custodian. A submission receipt is only the start of the record trail.",
            "Property and parcel identifiers, record categories, date range, known permit/case number, prior request number, and any agency referral.",
            List.of(
                step("Choose the holder, not just the state", "Municipal construction records and state environmental records may have different custodians. NJ's OPRA guidance asks requesters to direct requests to the appropriate holder. Our route plan names one agency and record category per request, avoiding an unbounded request for everything about the property.", "New Jersey official request guidance", "https://www.nj.gov/opra/home/request-records.shtml"),
                step("Use the channel's actual requirements", "Forms differ. Suffolk Health Department's application requests a full tax-map number and one property per request; current county instructions should be checked before submission. Assemble identifiers first, and confirm fees or physical submission requirements rather than assuming email is sufficient.", "Suffolk Health records application", "https://www.suffolkcountyny.gov/Portals/0/documentsforms/healthservices/Wastewater%20Management/Forms/APPLICATION_FOR_PUBLIC_ACCESS_TO_RECORDS_revised_2023.05.pdf"),
                step("Track acknowledgment, referral, and delivery separately", "Our case ledger preserves the request ID, channel, date, reply, next action, and check date. Summit's portal explicitly supports request status and communications. An automated receipt is not the requested document; a referral needs a new route decision, not a completed-case label.", "Summit request portal guidance", "https://www.cityofsummit.org/905/OPRA-Requests")),
            "Please provide [specific existing record categories] for [address; parcel], [date range], reference [number]. If another custodian holds these records, please identify that office. Please advise of any applicable fee before chargeable work proceeds.",
            "Templates are research aids, not legal advice. Follow the agency's current form, purpose disclosures, exemptions, and fee instructions. No external message or paid request is sent just because a draft exists.", "agency_follow_up", List.of("wayne-nj", "suffolk-ny", "new-york-city")),
        new Entry("records-or-tank-sweep", "problem", "", "Choosing the next step",
            "Oil tank records or a tank sweep: which question comes first?",
            "Records investigate documented history. A physical survey investigates present conditions. Choose the next action by the unresolved question, not by assuming one replaces the other.",
            "Known tank status, existing paperwork, the specific concern, prior search or survey results, transaction deadline, and the property address.",
            List.of(
                step("If the gap is paperwork, locate the file", "When a known removal is missing its documentation, research can target permits and completion records. Maplewood's removal-certificate record category is one concrete example. A focused request may clarify what the issuer recorded without ordering unrelated work.", "Maplewood removal-certificate record category", MAPLEWOOD),
                step("If the gap is physical presence, name that limit", "An empty online search cannot establish whether an unrecorded tank is on the property. Westchester's PBS map, for example, describes a dated subset of facilities. Where location or presence remains the question, discuss an appropriately scoped physical investigation with a qualified professional.", "Westchester historic PBS map coverage", "https://giswww.westchestergov.com/arcgis/rest/services/DataHub_EnvironmentandPlanning/MapServer/165/iteminfo"),
                step("If timing is tight, separate parallel tracks", "Our action list distinguishes records still pending from decisions that need professional input now. A tank report can add documentary history, as NYC's FDNY route illustrates, but research does not replace an inspection or an urgent response. Share the actual deadline at intake so open agency requests remain visible.", "NYC tank-specific record route", FDNY)),
            "The unresolved question is [historic work / physical presence / conflicting documents]. Existing evidence is [list]. Please identify the records that address the historic portion and the gaps that remain outside record research.",
            "Oil, strong odor, or suspected active leakage should leave this research workflow for the official safety route. Research is not an emergency service or a tank-free certification.", "choose_next_step", List.of("wayne-nj", "westchester-ny", "missing-removal-records"))
    );
    public static final List<Entry> ALL = Stream.concat(AREAS.stream(), PROBLEMS.stream()).toList();
    public static Entry find(String slug) {
        return ALL.stream().filter(e -> e.slug().equals(slug)).findFirst().orElse(null);
    }
    public static List<ServiceRoute> routes() {
        return Stream.concat(Stream.of(
            new ServiceRoute("research:areas", "Local oil tank record research routes", HUB, "Research directory", "0.8", REVIEWED, REVIEWED.plusMonths(3)),
            new ServiceRoute("research:examples", "Oil tank research walkthroughs", "/research-examples/", "Public-source walkthroughs", "0.7", REVIEWED, REVIEWED.plusMonths(3))),
            ALL.stream().map(e -> new ServiceRoute(e.id(), e.title(), e.path(), e.locality(), "0.7", REVIEWED, e.slug().equals("brookhaven-ny") ? LocalDate.of(2026, 9, 28) : REVIEWED.plusMonths(3)))).toList();
    }
}
