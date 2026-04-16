package owner.buriedoiltank.web;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import owner.buriedoiltank.config.SiteProperties;
import org.springframework.stereotype.Service;

@Service
public class ApiRequestProtectionService {
    private static final Duration LEAD_WINDOW = Duration.ofMinutes(15);
    private static final Duration EVENT_WINDOW = Duration.ofMinutes(5);
    private static final int LEAD_LIMIT = 8;
    private static final int EVENT_LIMIT = 90;

    private final Origin expectedOrigin;
    private final Clock clock;
    private final Map<String, Deque<Instant>> requestBuckets = new ConcurrentHashMap<>();

    public ApiRequestProtectionService(SiteProperties siteProperties, Clock clock) {
        this.expectedOrigin = Origin.from(siteProperties.getBaseUrl());
        this.clock = clock;
    }

    public boolean isTrustedRequest(HttpServletRequest request) {
        String candidate = firstNonBlank(request.getHeader("Origin"), request.getHeader("Referer"));
        if (candidate == null) {
            return false;
        }

        try {
            return expectedOrigin.matches(Origin.from(URI.create(candidate)));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean tryConsumeLeadCapture(HttpServletRequest request) {
        return tryConsumeBucket("lead:" + clientIdentifier(request), LEAD_LIMIT, LEAD_WINDOW);
    }

    public boolean tryConsumeEvent(HttpServletRequest request) {
        return tryConsumeBucket("event:" + clientIdentifier(request), EVENT_LIMIT, EVENT_WINDOW);
    }

    private boolean tryConsumeBucket(String key, int limit, Duration window) {
        Deque<Instant> bucket = requestBuckets.computeIfAbsent(key, ignored -> new ArrayDeque<>());
        Instant now = Instant.now(clock);
        Instant earliestAllowed = now.minus(window);

        synchronized (bucket) {
            while (!bucket.isEmpty() && bucket.peekFirst().isBefore(earliestAllowed)) {
                bucket.removeFirst();
            }
            if (bucket.size() >= limit) {
                return false;
            }
            bucket.addLast(now);
            if (bucket.isEmpty()) {
                requestBuckets.remove(key, bucket);
            }
            return true;
        }
    }

    private static String clientIdentifier(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            int separator = forwardedFor.indexOf(',');
            return separator >= 0 ? forwardedFor.substring(0, separator).trim() : forwardedFor.trim();
        }
        return request.getRemoteAddr();
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first.trim();
        }
        if (second != null && !second.isBlank()) {
            return second.trim();
        }
        return null;
    }

    private record Origin(String scheme, String host, int port) {
        private static Origin from(URI uri) {
            return new Origin(
                    uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(),
                    uri.getHost() == null ? "" : uri.getHost().toLowerCase(),
                    effectivePort(uri)
            );
        }

        private boolean matches(Origin other) {
            return scheme.equals(other.scheme) && host.equals(other.host) && port == other.port;
        }

        private static int effectivePort(URI uri) {
            if (uri.getPort() != -1) {
                return uri.getPort();
            }
            return switch (uri.getScheme() == null ? "" : uri.getScheme().toLowerCase()) {
                case "https" -> 443;
                case "http" -> 80;
                default -> -1;
            };
        }
    }
}
