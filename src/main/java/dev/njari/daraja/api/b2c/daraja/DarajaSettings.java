package dev.njari.daraja.api.b2c.daraja;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */


@Data
@Configuration
@ConfigurationProperties(prefix = "daraja")
public class DarajaSettings {
    private String url;
    private String responseType;
    private ResultCodes resultCodes;
    private String authenticationUrl;
    private String key;
    private String secret;
    private String shortcode;
    private String initiatorName;
    private String securityCredential;
    private String commandId;
    private String processPayouts;
    private String queueTimeoutUrl;
    private String resultUrl;

    @Data
    public static class ResultCodes {
        private int success;
        private int userCancelled;
    }
}
