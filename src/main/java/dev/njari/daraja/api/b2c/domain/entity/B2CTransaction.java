package dev.njari.daraja.api.b2c.domain.entity;

import dev.njari.daraja.api.b2c.domain.enums.B2CTransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
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
    @Column(columnDefinition = "UUID", name = "id")
    private UUID id;

    @Column(nullable = false, precision = 20, scale = 2, name = "amount")
    private BigDecimal amount;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private B2CTransactionStatus status;

    // 3rd party (M-PESA) transaction ref
    @Column(name = "transaction_ref")
    private String transactionReference;

    @Column(name = "remarks")
    private String remarks;

    // whether this transaction is in a non-retriable failed/successful state
    @Column(name = "terminal")
    private boolean terminal;

    @Column(nullable = false, name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false, name = "last_modified")
    @LastModifiedDate
    private Instant lastModified;
}
