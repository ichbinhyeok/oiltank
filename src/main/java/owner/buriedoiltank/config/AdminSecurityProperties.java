package owner.buriedoiltank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "buried-oil-tank.admin")
public class AdminSecurityProperties {
    private String username = "admin";
    private String password = "tlsgur3108";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
