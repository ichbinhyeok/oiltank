package owner.buriedoiltank.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RecordResearchNotificationPropertiesTests {
    @Test
    void gmailReadinessRequiresUsernameAndAppPassword() {
        RecordResearchNotificationProperties properties = new RecordResearchNotificationProperties();
        properties.setEnabled(true);
        properties.setRecipient("operator@example.test");
        properties.setSender("intake@example.test");
        properties.setSmtpHost("smtp.gmail.com");

        assertThat(properties.configurationIssue()).isEqualTo("missing_smtp_username");
        properties.setSmtpUsername("operator@gmail.com");
        assertThat(properties.configurationIssue()).isEqualTo("missing_smtp_password");
        properties.setSmtpPassword("app-password");
        assertThat(properties.configurationIssue()).isNull();
    }
}
