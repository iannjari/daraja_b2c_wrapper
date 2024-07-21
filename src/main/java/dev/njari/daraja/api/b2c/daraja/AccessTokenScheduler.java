package dev.njari.daraja.api.b2c.daraja;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenScheduler {
    private String accessToken;
    private final DarajaSettings darajaSettings;

    /**
     * fetch access token every 1 hour
     */
    @Scheduled(fixedRate = (1000 * 60) * 60)
    public void fetchAccessToken() throws InterruptedException {
        try {
            accessToken = DarajaHttpClient.getB2cSafAccessToken(darajaSettings.getKey(),
                    darajaSettings.getSecret(), darajaSettings.getAuthenticationUrl());
        } catch (IOException e) {
            e.printStackTrace();
            log.info("Error when fetching access token: {}", e.getLocalizedMessage());
            Thread.sleep(1000 * 3);
            log.info("re-trying to fetch access token {}: ", e.getLocalizedMessage());
        }
    }
}
