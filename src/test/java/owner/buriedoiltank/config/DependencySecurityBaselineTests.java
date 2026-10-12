package owner.buriedoiltank.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.util.ServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import static org.assertj.core.api.Assertions.assertThat;

class DependencySecurityBaselineTests {
    @Test
    void jacksonRuntimeIncludesSeptemberSecurityFixes() {
        assertAtLeast(new JsonFactory().version().toString(), "2.21.7");
        assertAtLeast(new ObjectMapper().version().toString(), "2.21.7");
        assertAtLeast(tools.jackson.core.json.JsonFactory.builder().build().version().toString(), "3.1.7");
        assertAtLeast(tools.jackson.databind.json.JsonMapper.builder().build().version().toString(), "3.1.7");
    }

    @Test
    void springRuntimeIncludesViewRenderingSecurityFixesOnCompatibleBoot() {
        assertAtLeast(SpringBootVersion.getVersion(), "4.0.8");
        assertAtLeast(SpringVersion.getVersion(), "7.0.9");
        assertAtLeast(ServerInfo.getServerNumber(), "11.0.26");
    }

    private static void assertAtLeast(String actual, String minimum) {
        assertThat(actual).isNotNull();
        String[] actualParts = actual.split("[.\\-]");
        String[] minimumParts = minimum.split("\\.");
        for (int index = 0; index < minimumParts.length; index++) {
            int comparison = Integer.compare(Integer.parseInt(actualParts[index]), Integer.parseInt(minimumParts[index]));
            if (comparison > 0) return;
            assertThat(comparison).as("runtime %s must be at least %s", actual, minimum).isGreaterThanOrEqualTo(0);
        }
    }
}
