package owner.buriedoiltank.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Clock;
import owner.buriedoiltank.config.SiteProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ApiRequestProtectionServiceTests {
    @Test
    void forwardedForCannotEvadeLeadRateLimit() {
        SiteProperties properties = new SiteProperties();
        properties.setBaseUrl(URI.create("https://oiltankroute.com"));
        ApiRequestProtectionService service = new ApiRequestProtectionService(properties, Clock.systemUTC());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("203.0.113.10");
        for (int attempt = 0; attempt < 8; attempt++) {
            request.removeHeader("X-Forwarded-For");
            request.addHeader("X-Forwarded-For", "198.51.100." + attempt);
            assertThat(service.tryConsumeLeadCapture(request)).isTrue();
        }

        request.removeHeader("X-Forwarded-For");
        request.addHeader("X-Forwarded-For", "192.0.2.99");
        assertThat(service.tryConsumeLeadCapture(request)).isFalse();

        request.setRemoteAddr("203.0.113.11");
        assertThat(service.tryConsumeLeadCapture(request)).isTrue();
    }
}
