package owner.buriedoiltank.data;

import java.util.List;
import owner.buriedoiltank.data.ResearchCatalog.Entry;
import owner.buriedoiltank.data.ResearchCatalog.Step;

/** Public-source research plans, not evidence of a customer search or agency delivery. */
final class AdditionalResearchAreas {
    private AdditionalResearchAreas() {}
    private static Step s(String title, String body, String label, String url) { return new Step(title, body, label, url); }
    static final List<Entry> ALL = List.of(
        new Entry("ridgewood-nj", "area", "new-jersey", "Ridgewood · Bergen County",
            "Ridgewood oil tank permits and property record research",
            "Start by separating the assessor property card from the construction file. Ridgewood directs property-card requests through the Village Clerk.",
            "Street address, block and lot, claimed work year, and any contractor or permit reference. Confirm the property is in the Village of Ridgewood.",
            List.of(
                s("Request the property card through the Clerk", "The Assessor directs property-record-card requests to the Village Clerk via OPRA. Use the card to match the parcel, retaining the address and parcel reference with your notes; it is not a tank-removal certificate.", "Ridgewood Assessor", "https://www.ridgewoodnj.net/160/Assessor"),
                s("Name the construction documents separately", "The Village residents directory links its records-request route separately from applying for a building permit. Our research request names existing tank permits, final inspections, and completion attachments, rather than applying for new work.", "Ridgewood resident services", "https://www.ridgewoodnj.net/31/Residents"),
                s("Follow environmental references separately", "If the returned municipal file identifies a state case, retain that reference and pursue the state-held documents. Keep an unavailable construction file separate from an environmental search that has not yet been made.", "NJDEP records access", "https://dep.nj.gov/opra/")),
            "Please provide the property record card and existing tank-related construction permits, final inspections, and completion attachments for [address; block/lot], [date range]. Please identify the custodian for any category not held by your office.",
            "This is a Clerk-led research plan, not a verified online tank inventory. A property card or missing permit cannot establish present tank absence.", "find_records", List.of("missing-removal-records", "seller-says-tank-removed")),
        new Entry("livingston-nj", "area", "new-jersey", "Livingston · Essex County",
            "Livingston oil tank construction permit record research",
            "Livingston names a Construction Permit Summary Report as the building-record starting point. Use its references to pursue the actual tank paperwork.",
            "Exact property address, block and lot if known, approximate work date, and any existing permit summary or document reference.",
            List.of(
                s("Ask for the named permit summary", "The Township's records cover sheet directs building-record requests to the Construction Permit Summary Report and asks for the property address. Name that report instead of asking for a general statement about the property.", "Livingston records instructions", "https://www.livingstonnj.org/DocumentCenter/View/26261/NEW-OPRA-FORM-9_3_2024-Fillable-with-Cover-Sheet"),
                s("Turn summary entries into targeted requests", "Our next step is to identify tank or fuel-conversion entries and request their underlying permits, inspection results, and completion records. A summary entry is a reference to investigate, not a substitute for the attachment. The Township also cautions that a survey may not be on file.", "Livingston records cover sheet and form", "https://www.livingstonnj.org/DocumentCenter/View/26261/NEW-OPRA-FORM-9_3_2024-Fillable-with-Cover-Sheet"),
                s("Keep state files on their own track", "Where a document names an NJDEP case, request that file separately and compare its subject and dates with the municipal work. Do not infer environmental closure from construction history.", "NJDEP records access", "https://dep.nj.gov/opra/")),
            "Please provide the Construction Permit Summary Report for [address]. For tank-related work during [dates], please also provide the associated permit, inspection, and completion records, reference [number if known].",
            "The summary may identify relevant work without supplying its complete history. No tank certificate, survey, or environmental result is guaranteed to exist.", "find_records", List.of("conflicting-tank-documents", "maplewood-nj")),
        new Entry("millburn-nj", "area", "new-jersey", "Millburn / Short Hills · Essex County",
            "Millburn and Short Hills oil tank permit research",
            "Millburn separates SDL building-permit lookup from its NextRequest records workflow. Research connects a permit reference to the documents behind it.",
            "Millburn or Short Hills property address, block and lot, approximate tank-work year, and any SDL permit number.",
            List.of(
                s("Start with the Township's permit link", "Millburn's records instructions point to SDL for building permits and a separate property-card resource. Match the property first and preserve any permit number and displayed status.", "Millburn records portal instructions", "https://twp.millburn.nj.us/320/OPRA-Request-Form-PDF"),
                s("Track the underlying request in NextRequest", "The same instructions direct records requests to NextRequest, where the account shows requests, status, communications, and provided records. Preserve the request number. Account confirmation or an email receipt is not document delivery.", "Millburn NextRequest guidance", "https://twp.millburn.nj.us/320/OPRA-Request-Form-PDF"),
                s("Reconcile attachments with the original entry", "Our review compares the supplied work description and final inspection against the original permit entry. If a state environmental reference appears, follow it separately; unresolved differences remain in the brief.", "NJDEP state-file route", "https://dep.nj.gov/opra/")),
            "Please provide the underlying tank-work permit, final inspection, and completion documents for [Millburn/Short Hills address; block/lot], SDL reference [number], approximately [dates].",
            "A portal status does not establish what happened to every tank. Short Hills is included in this Township research route, not presented as a separate verified records system.", "find_records", List.of("conflicting-tank-documents", "summit-nj")),
        new Entry("west-orange-nj", "area", "new-jersey", "West Orange · Essex County",
            "West Orange oil tank property file requests",
            "Use the Township's municipal records form for a bounded document request, rather than confusing police records with a construction file.",
            "Property address, block and lot, date range, known permit reference, and a list of the specific missing documents.",
            List.of(
                s("Use the municipal records form", "The Township document center links the OPRA form. Start from that current municipal entry point; a police records bureau page is not evidence that police hold tank construction files.", "West Orange document center", "https://www.westorange.org/198/Document-Center"),
                s("Describe records and complete disclosures", "The form requests a specific record description and includes purpose disclosures, delivery preference, and a cost-authorization field. Follow those instructions accurately. Our proposed scope names tank permits, inspections, and completion papers rather than asking the Clerk to certify safety.", "West Orange municipal request form", "https://www.westorange.org/1601/OPRA-Request-Form"),
                s("Separate referrals from completed searches", "If the municipal response points to a state environmental case, record the referral and pursue that case through NJDEP. Until records arrive, label the work pending rather than concluding the file is clear.", "NJDEP records access", "https://dep.nj.gov/opra/")),
            "Please provide existing tank installation, removal or abandonment permits, final inspection records, and completion attachments for [address; block/lot], [date range]. Please identify any separate custodian and advise of charges before paid work.",
            "This is a verified request entry point, not a claim that a tank-specific public database or a removal certificate exists for the property.", "agency_follow_up", List.of("agency-record-request", "missing-removal-records")),
        new Entry("huntington-ny", "area", "new-york", "Huntington · Suffolk County",
            "Huntington oil tank building-record research",
            "Huntington routes FOIL by department. Prepare the tax-map number and distinguish building paperwork from a county Health Department search.",
            "Street address, Suffolk tax-map number, town or incorporated village, approximate work dates, and permit references.",
            List.of(
                s("Identify the department before sending", "Huntington administers FOIL department by department and links its departmental officers. Use the current list to route building records; do not rely on an old individual email copied from another page.", "Huntington departmental FOIL route", "https://www.huntingtonny.gov/town-attorney/file-a-foil"),
                s("Request documents, not an answer about safety", "The application asks for the record-holding department and encourages the Suffolk tax-map number for property searches. Our request names permits, inspection reports, and closeout attachments for a bounded period.", "Huntington FOIL application", "https://www.huntingtonny.gov/filestorage/13753/13773/Foil_Form_11-17-2021.pdf"),
                s("Keep county records separate", "The town request does not complete a Suffolk Health Department search. Where the question concerns county-held records, use the county route separately and compare identifiers across the returned files.", "Suffolk county services", "https://www.suffolkcountyny.gov/services/citizen-services")),
            "Please provide existing Building Department tank-related permits, inspection reports, and closeout attachments for [address; Suffolk tax-map number], [dates]. Please identify any village or other custodian holding the requested category.",
            "Departmental routing is verified; document availability and village jurisdiction still need property-level confirmation. A town response does not cover every county or state file.", "agency_follow_up", List.of("suffolk-ny", "agency-record-request")),
        new Entry("brookhaven-ny", "area", "new-york", "Brookhaven · Suffolk County",
            "Brookhaven oil tank permits and property documents",
            "Brookhaven offers a building property-document research route and a separate FOIL process. County Health and state DEC records are not Town-held files.",
            "Street address, district/section/block/lot, municipality or village, work dates, and known permit or request references.",
            List.of(
                s("Use the property-document research service", "The Building Division links online property-document requests and research appointments. Its page announces a digital-services transition for September 28, 2026. Recheck the page before choosing a submission method during that change.", "Brookhaven Building Division", "https://www.brookhavenny.gov/284/Building-Division"),
                s("Use FOIL for the appropriate Town department", "The Town FOIL instructions require an account and department selection. Keep a request acknowledgment distinct from the returned permit or inspection record. Use the current Town entry point rather than an obsolete bookmarked form.", "Brookhaven records requests", "https://www.brookhavenny.gov/208/Freedom-of-Information-Law-Application"),
                s("Do not ask the Town to clear county files", "Brookhaven explicitly lists county Health Department and NYS DEC records among records it does not hold. Our plan splits those searches from Town construction records so a Town no-records response cannot stand in for a county or state result.", "Brookhaven records not held by the Town", "https://www.brookhavenny.gov/208/Freedom-of-Information-Law-Application")),
            "Please provide tank-related building permits, inspection results, and completion attachments for [address; tax-map number], [dates]. Please confirm the current property-document request route during the digital-services transition.",
            "The upcoming portal change needs a fresh check before submission. A Town search excludes county Health and state DEC files and does not establish physical tank absence.", "find_records", List.of("suffolk-ny", "missing-removal-records")),
        new Entry("islip-ny", "area", "new-york", "Islip · Suffolk County",
            "Islip oil tank building permits and inspection records",
            "Islip separates Building Department records from other FOIL documents. Choose the correct branch before requesting tank-related paperwork.",
            "Address, Suffolk tax-map number, village if applicable, approximate work date, permit number, and the specific missing record.",
            List.of(
                s("Choose Building Department records", "Islip's FOIL page has a building-only branch for permits, inspection reports, plans, surveys, and occupancy certificates. Planning, Engineering, and ZBA records use the other-documents branch.", "Islip online FOIL branches", "https://islipny.gov/foil"),
                s("Distinguish an issued permit from closeout", "The Building Division describes separate Permits and Records responsibilities, with Records handling inspections, certifications, and final closeout. Our research asks for the completion evidence behind the permit rather than treating issuance as completion.", "Islip Building Division and Records Office", "https://www.islipny.gov/departments/planning-and-development/building-division"),
                s("Keep excluded categories and county files visible", "The FOIL landing page lists categories not released through that workflow, including violations and rental/accessory-apartment documentation. Do not silently expand a building-file result into those categories or into a Suffolk Health search.", "Islip FOIL scope notices", "https://islipny.gov/foil")),
            "Please provide existing tank-related building permits, inspection reports, and closeout documents for [address; tax-map number], reference [permit], [dates]. The request concerns existing records, not a new inspection appointment.",
            "A building closeout entry is not environmental clearance. County files and categories outside this portal require separate routing; village jurisdiction must be confirmed.", "find_records", List.of("suffolk-ny", "conflicting-tank-documents")),
        new Entry("oyster-bay-ny", "area", "new-york", "Oyster Bay · Nassau County",
            "Oyster Bay oil tank municipal records and Nassau verification",
            "Keep Oyster Bay's Town Clerk records request separate from Nassau's small heating-oil tank verification letter. The two routes answer different parts of the history.",
            "Exact property address, municipality or incorporated village, Nassau parcel identifiers, tank use and size if known, and work dates.",
            List.of(
                s("Request Town-held documents through the Clerk", "Oyster Bay's FOIL page names the Town Clerk records-access route and accepts written requests through several channels. Describe the particular existing permit and inspection documents needed; check the page for current instructions.", "Oyster Bay Town Clerk FOIL", "https://oysterbaytown.com/departments/town-clerk/freedom-of-information-law-foil/"),
                s("Search the county verification route separately", "For an eligible small heating-oil tank, Nassau's homeowner route is a separate starting point for removal or abandonment verification. A Town permit does not substitute for a county letter, and the letter does not substitute for every municipal attachment.", "Nassau homeowner tank route", "https://www.nassaucountyny.gov/2059/Homeowner-Small-Fuel-Oil-Tank-Removals"),
                s("Match issuers and confirm village jurisdiction", "Our comparison preserves the issuer, address, recorded action, and date on each document. Confirm who holds municipal records for an incorporated-village property before sending; this Town route does not claim verified coverage of every village archive.", "Oyster Bay records-access guidance", "https://oysterbaytown.com/departments/town-clerk/freedom-of-information-law-foil/")),
            "Please provide Town-held tank permits, inspection results, and completion documents for [address; parcel], [dates]. Please identify any village or other custodian if these records are not held by the Town.",
            "Town and county record scopes overlap but are not interchangeable. Neither a no-records reply nor a verification letter establishes that no other tank remains.", "find_records", List.of("nassau-ny", "seller-says-tank-removed"))
    );
}
