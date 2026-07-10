package owner.buriedoiltank.web;

import java.util.List;
import owner.buriedoiltank.data.RouteFamily;
import owner.buriedoiltank.ops.AdminService;
import owner.buriedoiltank.pages.SitePageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SiteController {
    private final SitePageService sitePageService;
    private final AdminService adminService;

    public SiteController(SitePageService sitePageService, AdminService adminService) {
        this.sitePageService = sitePageService;
        this.adminService = adminService;
    }

    @GetMapping({"/", ""})
    public String home(Model model) {
        model.addAttribute("page", sitePageService.homePage());
        return "home";
    }

    @GetMapping({"/about", "/about/"})
    public String about(Model model) {
        model.addAttribute("page", sitePageService.staticPage("about"));
        return "static";
    }

    @GetMapping({"/methodology", "/methodology/"})
    public String methodology(Model model) {
        model.addAttribute("page", sitePageService.staticPage("methodology"));
        return "static";
    }

    @GetMapping({"/contact", "/contact/"})
    public String contact(Model model) {
        model.addAttribute("page", sitePageService.staticPage("contact"));
        return "static";
    }

    @GetMapping({"/privacy", "/privacy/"})
    public String privacy(Model model) {
        model.addAttribute("page", sitePageService.staticPage("privacy"));
        return "static";
    }

    @GetMapping({"/terms", "/terms/"})
    public String terms(Model model) {
        model.addAttribute("page", sitePageService.staticPage("terms"));
        return "static";
    }

    @GetMapping({"/not-government-affiliated", "/not-government-affiliated/"})
    public String notGovernmentAffiliated(Model model) {
        model.addAttribute("page", sitePageService.staticPage("not-government-affiliated"));
        return "static";
    }

    @GetMapping({"/states", "/states/"})
    public String states(Model model) {
        model.addAttribute("page", sitePageService.statesHubPage());
        return "hub";
    }

    @GetMapping({"/states/{stateSlug}", "/states/{stateSlug}/"})
    public Object stateHub(@PathVariable String stateSlug, Model model) {
        try {
            if ("connecticut".equals(stateSlug) || "maine".equals(stateSlug)) {
                return permanentRedirect("/states/");
            }
            model.addAttribute("page", sitePageService.statePage(stateSlug));
            return "state";
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping({"/states/{stateSlug}/{routeSlug}", "/states/{stateSlug}/{routeSlug}/"})
    public Object stateRoute(@PathVariable String stateSlug, @PathVariable String routeSlug, Model model) {
        try {
            RouteFamily family = RouteFamily.fromPathSegment(routeSlug);
            if ("connecticut".equals(stateSlug) || "maine".equals(stateSlug)) {
                return permanentRedirect(nationalGuideFor(family));
            }
            if ("new-york".equals(stateSlug) && family == RouteFamily.BUYER_SELLER) {
                return permanentRedirect("/guides/buried-oil-tank-home-sale/");
            }
            if ("new-york".equals(stateSlug) && family == RouteFamily.SWEEP_AND_LOCATE) {
                return permanentRedirect("/guides/oil-tank-sweep-before-buying-house/");
            }
            if (family == RouteFamily.RECORDS_AND_PROOF && List.of("new-jersey", "new-york").contains(stateSlug)) {
                model.addAttribute("page", sitePageService.recordsNavigatorPage(stateSlug));
                return "records";
            }
            model.addAttribute("page", sitePageService.routePage(stateSlug, family));
            return "route";
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping({"/guides", "/guides/"})
    public String guides(Model model) {
        model.addAttribute("page", sitePageService.guidesHubPage());
        return "hub";
    }

    @GetMapping({"/routes", "/routes/"})
    public RedirectView routes(Model model) {
        return permanentRedirect("/guides/");
    }

    @GetMapping({
            "/states/new-york/counties/{countySlug}/heating-oil-spill-records",
            "/states/new-york/counties/{countySlug}/heating-oil-spill-records/"
    })
    public String countyIncident(@PathVariable String countySlug, Model model) {
        try {
            model.addAttribute("page", sitePageService.countyIncidentPage(countySlug));
            return "records";
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping({"/guides/{slug}", "/guides/{slug}/"})
    public String guide(@PathVariable String slug, Model model) {
        try {
            model.addAttribute("page", sitePageService.guidePage(slug));
            return "guide";
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping({"/admin", "/admin/"})
    public String admin(Model model) {
        model.addAttribute("page", adminService.buildPage());
        return "admin";
    }

    private static RedirectView permanentRedirect(String target) {
        RedirectView redirect = new RedirectView(target);
        redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirect;
    }

    private static String nationalGuideFor(RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> "/states/";
            case BUYER_SELLER -> "/guides/buried-oil-tank-home-sale/";
            case SWEEP_AND_LOCATE -> "/guides/oil-tank-sweep-before-buying-house/";
            case RECORDS_AND_PROOF -> "/guides/abandoned-oil-tank-records/";
            case REMOVAL_VS_ABANDONMENT -> "/guides/remove-vs-abandon-oil-tank/";
            case LEAK_AND_CLEANUP -> "/guides/leaking-heating-oil-tank-what-to-do/";
            case COST_DIRECTION -> "/guides/oil-tank-removal-cost/";
        };
    }
}
