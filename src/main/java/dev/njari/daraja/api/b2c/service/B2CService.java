package dev.njari.daraja.api.b2c.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.njari.daraja.api.b2c.domain.entity.B2CTransaction;
import dev.njari.daraja.api.b2c.daraja.dto.B2CRequest;
import dev.njari.daraja.api.b2c.daraja.DarajaHttpClient;
import dev.njari.daraja.api.b2c.daraja.DarajaSettings;
import dev.njari.daraja.api.b2c.daraja.PayoutService;
import dev.njari.daraja.api.b2c.repository.B2CTransactionRepository;
import dev.njari.daraja.api.cps.domain.dto.B2CResultDTO;
import dev.njari.daraja.api.cps.domain.dto.GwRequest;
import dev.njari.daraja.api.cps.kafka.B2CResultPublisher;
import dev.njari.daraja.exception.InternalServerException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.njari.daraja.api.b2c.domain.enums.B2CTransactionStatus.*;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@Service
@RequiredArgsConstructor
public class B2CService {

    private final B2CTransactionRepository b2CTransactionRepo;
    private final B2CResultPublisher b2CResultPublisher;
    private final ObjectMapper objectMapper;
    private final DarajaSettings darajaSettings;
    private final PayoutService payoutService;

    @Qualifier("customResourceLoader")
    private final ResourceLoader resourceLoader;

    @Value("${daraja.api.b2c.cert-path}")
    private String certPath;

    /**
     * processes a GwRequest by forwarding it to the payment gateway(M-PESA Daraja API)
     * @param request - GwRequest
     */
    public void processGwRequest(GwRequest request) {

        // validate
        String failureReason = validate(request);
        // if validation failed, save it as a failed transaction, in terminal state,
        // publish it to Kafka as a failed result
        if (!failureReason.isBlank()) {

            B2CTransaction failedTransaction = new B2CTransaction();
            failedTransaction.setAmount(BigDecimal.valueOf(request.getAmount()));
            failedTransaction.setStatus(FAILED);
            failedTransaction.setTerminal(true);
            failedTransaction.setRemarks(failureReason);
            failedTransaction = saveAndPublish(failedTransaction, request);
            return;
        }

        // process requests passing validations
        B2CTransaction transaction = new B2CTransaction();
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setStatus(PENDING);
        transaction.setTerminal(true);
        transaction.setRemarks("Processing pending");

        // save with a status of PENDING initially, publish
        transaction = saveAndPublish(transaction, request);

        // generate request to payment gateway
        B2CRequest pgRequest = createB2CRequest(request, transaction);

        payoutService.processPayout(pgRequest);
        transaction.setStatus(PROCESSING);
        saveAndPublish(transaction, request);
    }

    /**
     * validates request
     * returns an empty string if valid
     * @param request - GwRequest
     * @return - String
     */
    private String validate(GwRequest request) {
        if (request.getId() == null) return "id is null";
        if (request.getMobileNumber() == null) return "the target MSISDN number is null";
        if (request.getMobileNumber().isBlank()) return "the target MSISDN number is blank";

        // check for E.164 compliance
        Pattern p = Pattern.compile("^\\+[1-9]\\d{1,14}$");
        String cleanNumber= request.getMobileNumber().replace(" ","");
        Matcher m = p.matcher(cleanNumber);
        if (!m.matches()) return "the target MSISDN number is not E.164 format compliant";

        // check for Kenyan MSISDN validity
        Pattern p1 = Pattern.compile("^\\+254\\d{9}$");
        Matcher m1 = p1.matcher(cleanNumber);
        if (!m1.matches()) return "the target MSISDN number is not a valid Kenyan MSISDN";

        // validate amount is within valid range
        if (request.getAmount()>15000 || request.getAmount() <10) return "amount is not within range KES 10 - 150000";

        return "";
    }

    /**
     * saves b2c tr and publishes result to Kafka
     * @param tr - B2CTransaction
     * @param request - GwRequest
     */
    @Transactional
    private B2CTransaction saveAndPublish(B2CTransaction tr, GwRequest request) {

        tr = b2CTransactionRepo.save(tr);
        B2CResultDTO result = new B2CResultDTO();
        result.setId(result.getId()); // id that came from requesting service
        result.setStatus(tr.getStatus());
        result.setRemarks(tr.getRemarks());
        result.setRef((tr.getTransactionReference() == null)? "" : tr.getTransactionReference());

        String message = null;
        try {
            message = objectMapper.writeValueAsString(request);
            b2CResultPublisher.publishInvokeStkRequest(message);

        } catch (JsonProcessingException | InternalServerException e) {
            // update of transaction as not published to avoid roll-back of JPA transaction
            tr.setLastPublishingFailed(true);
            tr = b2CTransactionRepo.save(tr);
        }

        return tr;
    }

    public B2CRequest createB2CRequest(GwRequest gwRequest, B2CTransaction tr) {

        String b2cPassword = darajaSettings.getSecurityCredential();

        B2CRequest request = new B2CRequest();

        request.setOriginatorConversationID(tr.getId().toString());
        request.setInitiatorName(darajaSettings.getInitiatorName());
        request.setSecurityCredential(
                DarajaHttpClient.generateSecurityCredentials(b2cPassword, certPath, resourceLoader));
        request.setPartyA(darajaSettings.getShortcode());
        request.setPartyB(gwRequest.getMobileNumber().replace("+", ""));
        request.setAmount(String.valueOf(tr.getAmount()));
        request.setRemarks("");
        request.setOccassion("PAYOUT");
        request.setCommandID(darajaSettings.getCommandId());
        request.setQueueTimeOutURL(darajaSettings.getQueueTimeoutUrl());
        request.setResultURL(darajaSettings.getResultUrl());
        return request;
    }
}
