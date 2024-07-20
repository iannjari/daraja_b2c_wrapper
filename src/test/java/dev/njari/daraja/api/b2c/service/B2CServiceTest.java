package dev.njari.daraja.api.b2c.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.njari.daraja.api.b2c.domain.entity.B2CTransaction;
import dev.njari.daraja.api.b2c.repository.B2CTransactionRepository;
import dev.njari.daraja.api.cps.domain.dto.GwRequest;
import dev.njari.daraja.api.cps.kafka.B2CResultPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static dev.njari.daraja.api.b2c.domain.enums.B2CTransactionStatus.FAILED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@ExtendWith(MockitoExtension.class)
class B2CServiceTest {

    @InjectMocks
    B2CService b2CService;

    @Mock
    B2CTransactionRepository b2CTransactionRepo;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    B2CResultPublisher resultPublisher;

    @Test
    @DisplayName("Given null id, should mark request as failed")
    void givenNullIdFails() {
        // given
        GwRequest request = new GwRequest();
        request.setAmount(100);
        request.setMobileNumber("+254710000000");

        // act
        b2CService.processGwRequest(request);

        // verify, and get arguments of verified mock
        ArgumentCaptor<B2CTransaction> argumentCaptor = ArgumentCaptor.forClass(B2CTransaction.class);
        verify(b2CTransactionRepo).save(argumentCaptor.capture());
        B2CTransaction capturedEntity = argumentCaptor.getValue();

        // assert
        assertEquals(FAILED, capturedEntity.getStatus());
        assertEquals("id is null", capturedEntity.getRemarks());
    }

    @Test
    @DisplayName("Given null MSISDN, should mark request as failed")
    void givenNullMSISDNFails() {
        // given
        GwRequest request = new GwRequest();
        request.setId(UUID.randomUUID());
        request.setAmount(100);

        // act
        b2CService.processGwRequest(request);

        // verify, and get arguments of verified mock
        ArgumentCaptor<B2CTransaction> argumentCaptor = ArgumentCaptor.forClass(B2CTransaction.class);
        verify(b2CTransactionRepo).save(argumentCaptor.capture());
        B2CTransaction capturedEntity = argumentCaptor.getValue();

        // assert
        assertEquals(FAILED, capturedEntity.getStatus());
        assertEquals("the target MSISDN number is null", capturedEntity.getRemarks());
    }

    @Test
    @DisplayName("Given blank MSISDN, should mark request as failed")
    void givenBlankMSISDNFails() {
        // given
        GwRequest request = new GwRequest();
        request.setId(UUID.randomUUID());
        request.setAmount(100);
        request.setMobileNumber("");

        // act
        b2CService.processGwRequest(request);

        // verify, and get arguments of verified mock
        ArgumentCaptor<B2CTransaction> argumentCaptor = ArgumentCaptor.forClass(B2CTransaction.class);
        verify(b2CTransactionRepo).save(argumentCaptor.capture());
        B2CTransaction capturedEntity = argumentCaptor.getValue();

        // assert
        assertEquals(FAILED, capturedEntity.getStatus());
        assertEquals("the target MSISDN number is blank", capturedEntity.getRemarks());
    }

    @Test
    @DisplayName("Given invalid MSISDN, should mark request as failed")
    void givenInvalidMSISDNFails() {
        // given
        GwRequest request = new GwRequest();
        request.setId(UUID.randomUUID());
        request.setAmount(100);
        request.setMobileNumber("254710000000");

        // act
        b2CService.processGwRequest(request);

        // verify, and get arguments of verified mock
        ArgumentCaptor<B2CTransaction> argumentCaptor = ArgumentCaptor.forClass(B2CTransaction.class);
        verify(b2CTransactionRepo).save(argumentCaptor.capture());
        B2CTransaction capturedEntity = argumentCaptor.getValue();

        // assert
        assertEquals(FAILED, capturedEntity.getStatus());
        assertEquals("the target MSISDN number is not E.164 format compliant", capturedEntity.getRemarks());
    }

    @Test
    @DisplayName("Given invalid MSISDN, should mark request as failed")
    void givenInvalidMSISDNFails1() {
        // given
        GwRequest request = new GwRequest();
        request.setId(UUID.randomUUID());
        request.setAmount(100);
        request.setMobileNumber("+256710000000");

        // act
        b2CService.processGwRequest(request);

        // verify, and get arguments of verified mock
        ArgumentCaptor<B2CTransaction> argumentCaptor = ArgumentCaptor.forClass(B2CTransaction.class);
        verify(b2CTransactionRepo).save(argumentCaptor.capture());
        B2CTransaction capturedEntity = argumentCaptor.getValue();

        // assert
        assertEquals(FAILED, capturedEntity.getStatus());
        assertEquals("the target MSISDN number is not a valid Kenyan MSISDN", capturedEntity.getRemarks());
    }

    @Test
    @DisplayName("Given invalid amount, should mark request as failed")
    void givenInvalidAmountFails() {
        // given
        GwRequest request = new GwRequest();
        request.setId(UUID.randomUUID());
        request.setAmount(1);
        request.setMobileNumber("+254710000000");

        // act
        b2CService.processGwRequest(request);

        // verify, and get arguments of verified mock
        ArgumentCaptor<B2CTransaction> argumentCaptor = ArgumentCaptor.forClass(B2CTransaction.class);
        verify(b2CTransactionRepo).save(argumentCaptor.capture());
        B2CTransaction capturedEntity = argumentCaptor.getValue();

        // assert
        assertEquals(FAILED, capturedEntity.getStatus());
        assertEquals("amount is not within range KES 10 - 150000", capturedEntity.getRemarks());
    }
}