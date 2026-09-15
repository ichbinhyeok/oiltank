package owner.buriedoiltank.leads;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import owner.buriedoiltank.config.RecordResearchNotificationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SmtpResearchMailGateway implements ResearchMailGateway {
    private final RecordResearchNotificationProperties properties;

    public SmtpResearchMailGateway(RecordResearchNotificationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void send(String recipient, String sender, String subject, String body) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(properties.getSmtpHost());
        mailSender.setPort(properties.getSmtpPort());
        mailSender.setUsername(properties.getSmtpUsername());
        mailSender.setPassword(properties.getSmtpPassword());
        mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.auth", Boolean.toString(!properties.getSmtpUsername().isBlank()));
        javaMailProperties.put("mail.smtp.starttls.enable", Boolean.toString(properties.isStartTls()));
        javaMailProperties.put("mail.smtp.connectiontimeout", "5000");
        javaMailProperties.put("mail.smtp.timeout", "5000");
        javaMailProperties.put("mail.smtp.writetimeout", "5000");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setFrom(sender);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
