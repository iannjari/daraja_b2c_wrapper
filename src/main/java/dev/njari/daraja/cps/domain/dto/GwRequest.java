package dev.njari.daraja.cps.domain.dto;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

import lombok.Data;
import java.util.UUID;

/**
 * DTO for request from Core Payment system
 */

@Data
public class GwRequest {

    private UUID id;
    private double amount;
    private String mobileNumber;
}
