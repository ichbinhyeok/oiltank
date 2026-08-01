package owner.buriedoiltank.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import owner.buriedoiltank.ops.RouteInventoryService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class TrailingSlashRedirectFilter extends OncePerRequestFilter {
    private final Set<String> canonicalPublicPaths;

    public TrailingSlashRedirectFilter(RouteInventoryService routeInventoryService) {
        Set<String> paths = new HashSet<>(Set.of(
                "/about/",
                "/methodology/",
                "/contact/",
                "/privacy/",
                "/terms/",
                "/not-government-affiliated/",
                "/states/",
                "/guides/",
                "/states/new-york/counties/westchester/heating-oil-spill-records/",
                "/states/new-york/counties/nassau/heating-oil-spill-records/",
                "/states/new-york/counties/suffolk/heating-oil-spill-records/"
        ));
        routeInventoryService.entries().forEach(entry -> paths.add(entry.path()));
        this.canonicalPublicPaths = Set.copyOf(paths);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        boolean safeMethod = "GET".equalsIgnoreCase(request.getMethod()) || "HEAD".equalsIgnoreCase(request.getMethod());
        String canonicalPath = path == null ? null : path + "/";
        if (safeMethod && path != null && !path.endsWith("/") && canonicalPublicPaths.contains(canonicalPath)) {
            String query = request.getQueryString();
            response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
            response.setHeader("Location", query == null || query.isBlank() ? canonicalPath : canonicalPath + "?" + query);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
