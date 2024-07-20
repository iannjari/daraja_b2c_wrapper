package dev.njari.daraja.api.cps.kafka;

import dev.njari.daraja.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@RequiredArgsConstructor
@Component
public class B2CResultPublisher {

    @Value("${producer..topic.b2c_result}")
    private String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    public void publishInvokeStkRequest(final String message) {

        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topicName, message);

            kafkaTemplate.flush();

        } catch (Exception ex) {
            throw new InternalServerException("Error occurred publishing b2c result " +  message, ex);
        }
    }

}
