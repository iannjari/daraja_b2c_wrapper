package dev.njari.daraja.api.b2c.http_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */
@Data
public class DarajaError {
    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("errorCode")
    private String errorCode;
}
