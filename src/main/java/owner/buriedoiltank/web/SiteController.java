package owner.buriedoiltank.web;

import owner.buriedoiltank.data.RouteFamily;
import owner.buriedoiltank.ops.AdminService;
import owner.buriedoiltank.pages.SitePageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String stateHub(@PathVariable String stateSlug, Model model) {
        try {
            model.addAttribute("page", sitePageService.statePage(stateSlug));
            return "state";
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping({"/states/{stateSlug}/{routeSlug}", "/states/{stateSlug}/{routeSlug}/"})
    public String stateRoute(@PathVariable String stateSlug, @PathVariable String routeSlug, Model model) {
        try {
            model.addAttribute("page", sitePageService.routePage(stateSlug, RouteFamily.fromPathSegment(routeSlug)));
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
    public String routes(Model model) {
        model.addAttribute("page", sitePageService.routesHubPage());
        return "hub";
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
}
