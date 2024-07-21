package dev.njari.daraja.api.cps.domain.dto;

import dev.njari.daraja.api.b2c.domain.enums.B2CTransactionStatus;
import lombok.Data;

import java.util.UUID;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@Data
public class B2CResultDTO {

    private UUID id;

    private B2CTransactionStatus status;

    private String ref;

    private String remarks;
}
