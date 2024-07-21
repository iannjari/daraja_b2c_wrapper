package dev.njari.daraja.api.cps.domain.dto;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

import lombok.Data;
import org.gradle.internal.impldep.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * DTO for request from Core Payment system
 */

@Data
public class GwRequest {

    private UUID id;
    private double amount;
    @JsonProperty("mobile_number")
    private String mobileNumber;
}
