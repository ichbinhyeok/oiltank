package owner.buriedoiltank;

import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.config.AdminSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SiteProperties.class, AdminSecurityProperties.class})
public class BuriedOilTankVerdictApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuriedOilTankVerdictApplication.class, args);
	}

}
