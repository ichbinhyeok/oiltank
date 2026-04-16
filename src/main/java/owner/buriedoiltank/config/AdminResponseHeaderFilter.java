package owner.buriedoiltank.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AdminResponseHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (isAdminPath(request.getRequestURI())) {
            response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
            response.setHeader("Cache-Control", "no-store, no-cache, max-age=0, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
        }
        filterChain.doFilter(request, response);
    }

    private static boolean isAdminPath(String requestUri) {
        return "/admin".equals(requestUri) || requestUri.startsWith("/admin/");
    }
}
