package dev.njari.daraja.api.b2c.repository;

import dev.njari.daraja.api.b2c.domain.entity.B2CTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@Repository
public interface B2CTransactionRepository extends JpaRepository<B2CTransaction, UUID> {
}
