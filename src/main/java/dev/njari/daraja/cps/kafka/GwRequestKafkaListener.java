package dev.njari.daraja.cps.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.njari.daraja.cps.domain.dto.GwRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.shaded.com.google.protobuf.DynamicMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Optional;

/**
 * @author njari_mathenge
 * on 19/07/2024.
 * github.com/iannjari
 */

@KafkaListener
@RequiredArgsConstructor
public class GwRequestKafkaListener {

    private final ObjectMapper objectMapper;

    /**
     * listen for gw requests from kafka
     * @param cr - ConsumerRecord
     * @param ack - Acknowledgment
     */
    @KafkaListener(groupId = "${kafka.consumer_group}", topics = "${kafka.topic.gw_request}",
            containerFactory = "kafkaManualACKListenerByteArrayContainerFactory")
    public void listenToGwRequest(ConsumerRecord<String, DynamicMessage> cr, Acknowledgment ack) {

        Optional<GwRequest> requestOptional = Optional.ofNullable(objectMapper.convertValue(cr.value(), GwRequest.class));
//TODO:        optionalRequest.ifPresent(// do something);
        ack.acknowledge();
    }
}
