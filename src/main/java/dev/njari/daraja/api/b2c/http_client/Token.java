package dev.njari.daraja.api.b2c.http_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */
@Data
public class Token {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private long expiresIn;
}
