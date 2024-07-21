package dev.njari.daraja.api.b2c.http_client;

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
    private TransactionTypes transactionTypes;
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
        private String tillTillShortcode;
        private String key;
        private String secret;
        private String passkey;
    }

    @Getter
    @Setter
    public static class Api {
        private Stk stk;
        private Urls urls;
        private B2C b2c;
    }

    @Getter
    @Setter
    public static class Credentials {
        private String key;
        private String secret;
        private String passkey;
    }

    @Getter
    @Setter
    public static class TransactionTypes {
        private String paybill;
        private String buygoods;
    }

    @Getter
    @Setter
    public static class ResultCodes {
        private int success;
        private int userCancelled;
    }

    @Getter
    @Setter
    public static class Stk {
        private String processRequest;
        private String queryTransaction;
        private String callbackStk;
    }

    @Data
    public static class B2C {
        private String processPayouts;
        private String queueTimeoutUrl;
        private String resultUrl;
    }

    @Getter
    @Setter
    public static class Urls {
        private String authenticationUrl;
        private String pullTransactionsRegisterUrl;
        private String pullTransactions;
        private String transactionHistoryUrl;
    }
}
