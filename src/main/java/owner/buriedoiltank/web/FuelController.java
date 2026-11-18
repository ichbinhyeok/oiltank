package owner.buriedoiltank.web;
import owner.buriedoiltank.pages.FuelPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class FuelController {
    private final FuelPageService pages;
    public FuelController(FuelPageService pages) { this.pages = pages; }
    @GetMapping({"/heating-oil-prices", "/heating-oil-prices/"})
    public String prices(Model model) { model.addAttribute("page", pages.page("us")); return "fuelPrices"; }
    @GetMapping({"/heating-oil-delivery", "/heating-oil-delivery/"})
    public String delivery(Model model) { model.addAttribute("page", pages.delivery()); return "fuelDelivery"; }
    @GetMapping({"/heating-oil-guides", "/heating-oil-guides/"})
    public String guides(Model model) { model.addAttribute("page", pages.guides()); return "fuelGuideHub"; }
    @GetMapping({"/{slug:100-gallons-heating-oil|150-gallons-heating-oil|cash-heating-oil|automatic-vs-will-call-heating-oil|heating-oil-minimum-delivery|fixed-vs-capped-heating-oil-prices|how-long-does-heating-oil-last|heating-oil-vs-propane}",
        "/{slug:100-gallons-heating-oil|150-gallons-heating-oil|cash-heating-oil|automatic-vs-will-call-heating-oil|heating-oil-minimum-delivery|fixed-vs-capped-heating-oil-prices|how-long-does-heating-oil-last|heating-oil-vs-propane}/"})
    public String guide(@PathVariable String slug, Model model) { model.addAttribute("page", pages.guide(slug)); return "fuelGuide"; }
    @GetMapping({"/heating-oil-prices/{state}", "/heating-oil-prices/{state}/"})
    public String state(@PathVariable String state, Model model) {
        try { model.addAttribute("page", pages.page(state)); return "fuelPrices"; }
        catch (IllegalArgumentException error) { throw new ResponseStatusException(HttpStatus.NOT_FOUND); }
    }
    @GetMapping(value = "/heating-oil-prices/data.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> download() { return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=heating-oil-prices.csv").body(pages.csv()); }
}
