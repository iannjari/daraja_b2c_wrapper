package dev.njari.daraja.api.cps.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.njari.daraja.api.b2c.service.B2CService;
import dev.njari.daraja.api.cps.domain.dto.GwRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.shaded.com.google.protobuf.DynamicMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author njari_mathenge
 * on 19/07/2024.
 * github.com/iannjari
 */

@KafkaListener
@RequiredArgsConstructor
@Service
public class GwRequestKafkaListener {

    private final ObjectMapper objectMapper;
    private final B2CService b2CService;

    /**
     * listen for gw requests from kafka
     * @param cr - ConsumerRecord
     * @param ack - Acknowledgment
     */
    @KafkaListener(groupId = "${kafka.consumer_group}", topics = "${kafka.topic.gw_request}",
            containerFactory = "kafkaManualACKListenerByteArrayContainerFactory")
    public void listenToGwRequest(ConsumerRecord<String, DynamicMessage> cr, Acknowledgment ack) {

        try {

            Optional<GwRequest> requestOptional = Optional.ofNullable(objectMapper.convertValue(cr.value(), GwRequest.class));
            // if message cannot be deserialized to GwRequest, skip, to avoid kafka poison pill
            requestOptional.ifPresent(b2CService::processGwRequest);
        } catch (Exception e) {
            // skip, to avoid kafka poison pill
        }
        // acknowledge so that the Kafka broker moves this consumer group to next offset
        ack.acknowledge();
    }
}
