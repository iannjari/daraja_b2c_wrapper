package dev.njari.daraja.b2c.domain.entity;

import dev.njari.daraja.b2c.domain.enums.B2CTransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@Entity
@Table(name = "b2c_transaction")
@Data
public class B2CTransaction {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private B2CTransactionStatus status;

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant lastModified;
}
