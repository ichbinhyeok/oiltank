package owner.buriedoiltank.leads;

public interface ResearchMailGateway {
    void send(String recipient, String sender, String subject, String body);
}
