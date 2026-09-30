package owner.buriedoiltank.data;

import java.util.List;
import owner.buriedoiltank.data.ResearchCatalog.Entry;
import owner.buriedoiltank.data.ResearchCatalog.Step;

/** Additional independently useful local routes; generic clerk links alone do not qualify. */
final class LaunchResearchAreas {
    private LaunchResearchAreas() {}
    private static Step s(String title, String body, String url) { return new Step(title, body, "Official local record instructions", url); }
    static final List<Entry> ALL = List.of(
        new Entry("south-orange-nj", "area", "new-jersey", "South Orange Village · Essex County",
            "South Orange oil tank removal and abandonment records",
            "Request the Building file through the Clerk, then check the tank-location survey, inspection and disposal paperwork behind the permit.",
            "South Orange property address, block/lot if known, permit number, approximate work year and whether the tank was removed or filled in place.",
            List.of(
                s("Use the Clerk route for an existing property file", "South Orange's Building FAQ directs prospective buyers seeking oil tank history through the Clerk's OPRA process. Start with a request for existing records, not a new tank-work application. Use the current Clerk link in the next step rather than relying on an old paper form.", "https://www.southorange.org/FAQ.aspx?QID=123"),
                s("Prepare the official OPRA submission", "The Clerk's current page links its online OPRA form. Identify the property and bounded work dates, then name the permit and missing attachments. Keep your submitted request and the eventual response separately; this worksheet prepares text but does not send the form.", "https://www.southorange.org/161/Clerks-Office"),
                s("Ask for the attachments that distinguish the work", "The Village's tank guidance identifies a tank-location survey, inspections and waste/scrap bills of lading. Request these existing attachments where applicable. If returned papers cite soil tests, a DEP case or a final report, keep those references separate from the local permit status.", "https://www.southorange.org/686/Oil-Tank-Abandonment")),
            "Please provide existing tank removal or abandonment permits, tank-location surveys, inspection records and waste/scrap bills of lading for [property; block/lot], [dates], permit [number]. Please include any referenced DEP case number and final report held in the file.",
            "A permit to fill a tank is not a record of removal. Listed document requirements do not prove those papers exist for a particular historic job. Agency review times and fees must be confirmed with the current channel.", "find_records", List.of("njdep-nfa-letter", "maplewood-nj")),
        new Entry("babylon-ny", "area", "new-york", "Town of Babylon · Suffolk County",
            "Babylon oil tank removal records and Environmental files",
            "Use the Town's Environmental tank-removal category, not only a Building permit request. Prepare the complete Suffolk tax-map reference.",
            "Physical address, district/section/block/lot (SCTM), approximate dates and known permit number. Confirm Town versus incorporated village custody.",
            List.of(
                s("Name the Environmental record category", "The Town's FOIL checklist lists storage tanks and Tank Removal under Environmental, separately from Building permits and Fire inspection reports. Describe the tank-removal file explicitly so a general Building-only request does not leave that record family unresolved.", "https://townofbabylonny.gov/DocumentCenter/View/174/Freedom-of-Information-Law-FOIL-Application"),
                s("Complete the parcel and delivery details", "The same application asks for the full SCTM identifier, physical address and whether you want review only. Follow its current Clerk submission instructions and fee disclosures. Keep a copy and reference when you send it; preparing the worksheet is not a submission.", "https://townofbabylonny.gov/DocumentCenter/View/174/Freedom-of-Information-Law-FOIL-Application"),
                s("Resolve the property reference without merging custodians", "The Town Assessor maintains property cards and related records that can help identify the parcel. Confirm whether the Town or an incorporated village holds the work file. A Town Environmental response does not cover Suffolk Health or a DEC spill file unless those records are specifically included.", "https://www.townofbabylonny.gov/133/Assessors-Office")),
            "Please provide existing Environmental storage-tank and tank-removal records, plus related Building permits and final inspections held by the Town, for [physical address; district/section/block/lot], [dates]. Known permit [number]. Please identify any separate village custodian.",
            "This is the Town of Babylon route, not the Village of Babylon's request process. Ask for existing documents; a custodian response is not a physical tank inspection.", "find_records", List.of("suffolk-ny", "nysdec-spill-documents")),
        new Entry("portland-or", "area", "oregon", "City of Portland · Oregon",
            "Portland oil tank permit and decommissioning records",
            "Separate Portland Fire & Rescue's installation/removal permits from Oregon DEQ decommissioning and cleanup files. A blank Portland Maps search is not the end of the trail.",
            "City jurisdiction, street address and any former address, permit or IVR number, approximate work year and DEQ project number if known.",
            List.of(
                s("Route tank permits to Fire & Rescue", "Portland's records guidance assigns residential heating-oil tank installation and removal permits to Fire & Rescue in the Public Safety service area. Do not assume a general building-record request or Portland Maps search includes that separate tank file.", "https://www.portland.gov/ppd/public-records"),
                s("Use the records portal, not an inspection booking", "In the City's records portal, choose Submit a Records Request and the public-record route, then the relevant bureau. The portal's My Records Request Center holds status and delivered records. A request acknowledgment or invoice is not the document itself.", "https://www.portland.gov/public-records/portal-guide"),
                s("Follow DEQ and historic jurisdiction separately", "For a decommissioning certificate or cleanup file, use the Oregon DEQ routes below. The City warns that some neighborhoods were outside Portland when tanks were installed, so historic City coverage can be incomplete. Record that coverage gap rather than treating an empty City file as proof of no tank.", "https://www.portland.gov/ppd/public-records")),
            "Please provide existing Fire & Rescue residential oil tank installation/removal permits and associated inspection records for [address; former address], [dates], permit/IVR [number]. If the area was outside City jurisdiction at the time, please identify any referral or coverage limitation.",
            "Portland City permits, DEQ certificates and cleanup records are distinct. This route does not cover every address in the Portland metro area. Self-service only; no Oregon research intake or agency sending.", "find_records", List.of("oregon-clean-decommissioning", "oregon-leaking-tank-records")),
        new Entry("bloomfield-nj", "area", "new-jersey", "Bloomfield Township · Essex County",
            "Bloomfield oil tank permits and completion records",
            "Check open and closed permits through Bloomfield's SDL link before requesting missing inspection or tank completion documents from the Clerk.",
            "Street address, block and lot, permit reference and approximate work year. Confirm Bloomfield Township, New Jersey jurisdiction.",
            List.of(
                s("Start with the permit index", "Bloomfield's records page links SDL for open and closed permits and explains account creation. Search the property and capture the permit reference. An index status is a starting point; retain the actual inspection or completion document separately.", "https://www.bloomfieldtwpnj.com/1491/Open-Public-Records"),
                s("Describe the missing attachment", "Use the linked online OPRA form or the Clerk's listed submission alternatives. Name the tank permit, final inspection and removal or abandonment completion documents for a bounded period. Check the current form requirements before sending.", "https://www.bloomfieldtwpnj.com/1491/Open-Public-Records"),
                s("Separate local work from environmental closure", "If the local file cites an NJDEP incident or NFA, pursue that reference separately. Record a missing local attachment as unresolved rather than inferring that a state environmental letter will replace the municipal completion record.", "https://dep.nj.gov/srp/unregulated/nfa-letter/")),
            "Please provide the oil tank permit, final inspection and completion attachments for [property; block/lot], permit [number], approximately [dates]. The SDL listing shows [reference] but I could not locate [document].",
            "A closed permit index entry does not itself provide every underlying document. Neither a portal omission nor a Clerk response proves no tank exists.", "find_records", List.of("njdep-nfa-letter", "montclair-nj")),
        new Entry("smithtown-ny", "area", "new-york", "Town of Smithtown · Suffolk County",
            "Smithtown oil tank building records and FOIL requests",
            "Prepare the Building Department's parcel-specific records request, then keep Suffolk Health and DEC searches separate.",
            "Section, block, lot(s), address, approximate work dates and permit reference. Confirm Town rather than an incorporated village's record jurisdiction.",
            List.of(
                s("Use the Building Department records form", "Smithtown's Building Department links its public-records application. The form asks for one parcel per request with section, block and lot. Prepare those identifiers before describing the specific tank permit, inspection or completion document you need.", "https://www.smithtownny.gov/109/Building-Department"),
                s("Keep the requested scope narrow", "Use the current form's delivery instructions and distinguish inspection of existing records from copies. A request number or acknowledgment is not the permit file. Track a date range and reference so a later clarification does not restart an unbounded search.", "https://smithtownny.gov/DocumentCenter/View/7749/App_FOIL_Form1--A"),
                s("Pursue county and state files independently", "A Town building file and Suffolk County Health records are different sets. If the paperwork mentions a spill number, add DEC as a separate source in your record plan. Do not let a Town-only no-record response stand in for those other searches.", "https://dec.ny.gov/news/foil")),
            "Please provide tank-related permits, final inspections and completion records for [address], section [section], block [block], lot [lot], during [dates], permit [number]. This request concerns one parcel.",
            "This is the Town Building Department route, not a countywide tank inventory. An incorporated village may have a different custodian.", "find_records", List.of("suffolk-ny", "nysdec-spill-documents")),
        new Entry("riverhead-ny", "area", "new-york", "Town of Riverhead · Suffolk County",
            "Riverhead oil tank permit and property-file requests",
            "Riverhead routes copies of permits and property documents through the Town Clerk's FOIL process, rather than an inspection-scheduling email.",
            "Property address, tax parcel, approximate work dates, permit number and the specific missing completion record.",
            List.of(
                s("Use the Clerk's record route", "Riverhead Building directs requests for permit, certificate and survey copies to the Town Clerk through FOIL. Follow the electronic FOIL link from the Building page. Do not send a historic file request to the separate inspection-scheduling address.", "https://www.townofriverheadny.gov/203/Building"),
                s("Specify the tank work and attachment", "List the existing tank removal or abandonment permit, final inspection and completion record you need. State the property identifiers and work period. Requesting a file does not schedule a new inspection or create a missing certificate.", "https://www.townofriverheadny.gov/203/Building"),
                s("Keep other custodians on the list", "The Clerk routes Town-held material to the appropriate department. Suffolk Health or DEC material may require another request. Use a separate source status for each so a Town referral remains a next action, not a completed property investigation.", "https://dec.ny.gov/news/foil")),
            "Please route this request for existing oil tank permits, final inspections and completion attachments to the appropriate Town department: [property; parcel], work [dates], permit [number].",
            "The Town's documents cannot establish the completeness of county or DEC files. No record found is not a physical tank search.", "find_records", List.of("suffolk-ny", "agency-record-request")),
        new Entry("southampton-ny", "area", "new-york", "Town of Southampton · Suffolk County",
            "Southampton oil tank property lookup and record requests",
            "Use the property-information lookup linked from the Town's FOIL form, then prepare a department-specific request for the missing document.",
            "Address/tax map number, department, date range and permit reference. Confirm Town versus Village of Southampton jurisdiction.",
            List.of(
                s("Check the property reference first", "Southampton's FOIL form links a property-information lookup. Use it to identify the record reference before requesting copies. A property listing is not a substitute for the actual tank work description or completed inspection attachment.", "https://www.southamptontownny.gov/FormCenter/46/210"),
                s("Choose the document delivery you need", "The Town form distinguishes inspecting records, copies and scans by email. It asks for department, dates and parcel-specific identifiers. Prepare the worksheet, then complete the current official form yourself; this site does not submit it.", "https://www.southamptontownny.gov/FormCenter/46/210"),
                s("Confirm jurisdiction before following up", "A Southampton postal address does not by itself identify whether Town or village files govern the work. Ask the named custodian if the particular record belongs elsewhere. Follow a county Health or DEC reference separately from the local permit request.", "https://dec.ny.gov/news/foil")),
            "Please provide scans of existing tank permits, inspection and completion documents for [property; tax map number], [dates], department [name], permit [number]. If another custodian holds this file, please identify it.",
            "This route describes the Town form, not the Village's process. A request submission is not an agency acknowledgment or document delivery.", "find_records", List.of("suffolk-ny", "nysdec-spill-documents")),
        new Entry("yonkers-ny", "area", "new-york", "City of Yonkers · Westchester County",
            "Yonkers oil tank building files and Fire Department records",
            "Use the Housing & Buildings historic-file route and keep any Fire Department tank record request separate from new permit filing.",
            "Property address, block and lot, approximate work dates and permit number; identify whether the needed file is Building or Fire-held.",
            List.of(
                s("Request the historic property file", "Housing & Buildings asks users to locate block and lot and make a file-review appointment. During digitization, its current instructions say a document link is sent instead of an office visit; the link may arrive after the appointment date.", "https://www.yonkersny.gov/217/Housing-Buildings"),
                s("Use Fire's separate records route", "The Fire Department lists an Incident or Record Search via the City's FOIL route. Use this if the missing tank document belongs to Fire. A new City Squared permit application is not the same thing as retrieval of an old tank file.", "https://www.yonkersny.gov/376/Fire-Department-Forms-Permit-Information"),
                s("Keep county evidence separate", "If a file cites Westchester petroleum bulk storage material, follow that county route as another source. Preserve the issuer and reference for each document. A digitization delay in the City file does not mean the property has no historic records.", "https://health.westchestergov.com/petroleum-bulk-storage")),
            "Please provide existing tank-related [Building/Fire] permits, final inspections and completion records for [property; block/lot], [dates], reference [number]. Please identify any historic material still awaiting digitization.",
            "Yonkers Building, City Fire, Westchester Health and DEC are separate possible custodians. This page does not promise every file is digitized or immediately delivered.", "find_records", List.of("westchester-ny", "nysdec-spill-documents"))
    );
}
