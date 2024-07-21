package dev.njari.daraja.api.b2c.http_client;

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
public class B2CRequest {

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("InitiatorName")
    private String initiatorName;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandID;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("PartyA")
    private String partyA;

    @JsonProperty("PartyB")
    private String partyB;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;

    @JsonProperty("ResultURL")
    private String resultURL;

    @JsonProperty("Occassion")
    private String occassion;
}
