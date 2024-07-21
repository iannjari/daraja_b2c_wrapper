package dev.njari.daraja.api.b2c.daraja;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    private Api api;
    private B2CConfig b2CConfig;
    private KyoskRetailers kyoskRetailers;


    @Getter
    @Setter
    public static class B2CConfig {
        private String key;
        private String secret;
        private String shortcode;
        private String initiatorName;
        private String securityCredential;
        private String commandId;
    }

    @Getter
    @Setter
    public static class KyoskRetailers {
        private String shortCode;
        private String tillNumber;
        private String key;
        private String secret;
        private String passkey;
    }

    @Data
    public static class Api {
        private Urls urls;
        private B2C b2c;
    }

    @Getter
    @Setter
    public static class ResultCodes {
        private int success;
        private int userCancelled;
    }

    @Data
    public static class B2C {
        private String processPayouts;
        private String queueTimeoutUrl;
        private String resultUrl;
    }

    @Data
    public static class Urls {
        private String authenticationUrl;
    }
}
