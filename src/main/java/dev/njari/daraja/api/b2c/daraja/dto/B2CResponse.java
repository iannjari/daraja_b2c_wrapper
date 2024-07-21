package dev.njari.daraja.api.b2c.daraja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */
@Data
@NoArgsConstructor
public class B2CResponse {

    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;
}
