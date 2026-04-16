package owner.buriedoiltank.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class CanonicalHostRedirectFilterTests {
    @Test
    void redirectsGetRequestsToCanonicalApexHost() throws Exception {
        CanonicalHostRedirectFilter filter = filterFor("https://oiltankroute.com");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/states/new-jersey/");
        request.setScheme("https");
        request.setServerName("www.oiltankroute.com");
        request.setServerPort(443);
        request.setQueryString("ref=test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(301, response.getStatus());
        assertEquals("https://oiltankroute.com/states/new-jersey/?ref=test", response.getHeader("Location"));
    }

    @Test
    void preservesMethodOnNonSafeRequests() throws Exception {
        CanonicalHostRedirectFilter filter = filterFor("https://oiltankroute.com");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/leads/capture");
        request.setScheme("https");
        request.setServerName("www.oiltankroute.com");
        request.setServerPort(443);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(308, response.getStatus());
        assertEquals("https://oiltankroute.com/api/leads/capture", response.getHeader("Location"));
    }

    @Test
    void skipsActuatorPaths() throws Exception {
        CanonicalHostRedirectFilter filter = filterFor("https://oiltankroute.com");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
        request.setScheme("http");
        request.setServerName("127.0.0.1");
        request.setServerPort(8080);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(200, response.getStatus());
        assertNull(response.getHeader("Location"));
    }

    private static CanonicalHostRedirectFilter filterFor(String baseUrl) {
        SiteProperties siteProperties = new SiteProperties();
        siteProperties.setBaseUrl(java.net.URI.create(baseUrl));
        return new CanonicalHostRedirectFilter(siteProperties);
    }
}
