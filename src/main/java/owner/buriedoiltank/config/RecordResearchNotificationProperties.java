package owner.buriedoiltank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "buried-oil-tank.notifications")
public class RecordResearchNotificationProperties {
    private boolean enabled;
    private String recipient = "";
    private String sender = "";
    private String smtpHost = "";
    private int smtpPort = 587;
    private String smtpUsername = "";
    private String smtpPassword = "";
    private boolean startTls = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getSmtpHost() { return smtpHost; }
    public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }
    public int getSmtpPort() { return smtpPort; }
    public void setSmtpPort(int smtpPort) { this.smtpPort = smtpPort; }
    public String getSmtpUsername() { return smtpUsername; }
    public void setSmtpUsername(String smtpUsername) { this.smtpUsername = smtpUsername; }
    public String getSmtpPassword() { return smtpPassword; }
    public void setSmtpPassword(String smtpPassword) { this.smtpPassword = smtpPassword; }
    public boolean isStartTls() { return startTls; }
    public void setStartTls(boolean startTls) { this.startTls = startTls; }

    public String configurationIssue() {
        if (!enabled) return null;
        if (isBlank(recipient)) return "missing_recipient";
        if (isBlank(sender)) return "missing_sender";
        if (isBlank(smtpHost)) return "missing_smtp_host";
        if (smtpPort < 1 || smtpPort > 65535) return "invalid_smtp_port";
        if (isBlank(smtpUsername)) return "missing_smtp_username";
        if (isBlank(smtpPassword)) return "missing_smtp_password";
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
