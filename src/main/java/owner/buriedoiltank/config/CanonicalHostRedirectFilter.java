package owner.buriedoiltank.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CanonicalHostRedirectFilter extends OncePerRequestFilter {
    private final URI canonicalBaseUrl;

    public CanonicalHostRedirectFilter(SiteProperties siteProperties) {
        this.canonicalBaseUrl = siteProperties.getBaseUrl();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (shouldSkip(request) || isLocalCanonicalHost() || matchesCanonicalRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        URI redirectTarget = buildRedirectTarget(request);
        boolean safeMethod = "GET".equalsIgnoreCase(request.getMethod()) || "HEAD".equalsIgnoreCase(request.getMethod());
        response.setStatus(safeMethod ? HttpStatus.MOVED_PERMANENTLY.value() : HttpStatus.PERMANENT_REDIRECT.value());
        response.setHeader("Location", redirectTarget.toString());
    }

    private boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null || path.startsWith("/actuator");
    }

    private boolean matchesCanonicalRequest(HttpServletRequest request) {
        return canonicalScheme().equalsIgnoreCase(request.getScheme())
                && canonicalHost().equalsIgnoreCase(request.getServerName())
                && canonicalPort() == effectivePort(request.getScheme(), request.getServerPort());
    }

    private URI buildRedirectTarget(HttpServletRequest request) {
        String query = request.getQueryString();
        String pathAndQuery = query == null || query.isBlank()
                ? request.getRequestURI()
                : request.getRequestURI() + "?" + query;
        return canonicalBaseUrl.resolve(pathAndQuery);
    }

    private String canonicalScheme() {
        return canonicalBaseUrl.getScheme() == null ? "https" : canonicalBaseUrl.getScheme();
    }

    private String canonicalHost() {
        return canonicalBaseUrl.getHost() == null ? "" : canonicalBaseUrl.getHost();
    }

    private boolean isLocalCanonicalHost() {
        String host = canonicalHost();
        return "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host);
    }

    private int canonicalPort() {
        return effectivePort(canonicalScheme(), canonicalBaseUrl.getPort());
    }

    private static int effectivePort(String scheme, int port) {
        if (port != -1) {
            return port;
        }
        if ("https".equalsIgnoreCase(scheme)) {
            return 443;
        }
        if ("http".equalsIgnoreCase(scheme)) {
            return 80;
        }
        return -1;
    }
}
