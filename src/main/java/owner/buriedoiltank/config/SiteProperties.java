package owner.buriedoiltank.config;

import java.net.URI;
import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "buried-oil-tank")
public class SiteProperties {
    private URI baseUrl = URI.create("http://localhost:8080");
    private Path storageRoot = Path.of("storage");
    private String analyticsMeasurementId = "";

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Path getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(Path storageRoot) {
        this.storageRoot = storageRoot;
    }

    public String getAnalyticsMeasurementId() {
        return analyticsMeasurementId;
    }

    public void setAnalyticsMeasurementId(String analyticsMeasurementId) {
        this.analyticsMeasurementId = analyticsMeasurementId;
    }
}
