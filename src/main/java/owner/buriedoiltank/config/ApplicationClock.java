package owner.buriedoiltank.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationClock {
    @Bean
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
