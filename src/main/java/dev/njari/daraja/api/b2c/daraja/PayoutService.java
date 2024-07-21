package dev.njari.daraja.api.b2c.daraja;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.njari.daraja.api.b2c.daraja.dto.B2CRequest;
import dev.njari.daraja.api.b2c.daraja.dto.B2CResponse;
import dev.njari.daraja.api.b2c.daraja.dto.DarajaError;
import dev.njari.daraja.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutService {

    private final ObjectMapper mapper;
    private final DarajaSettings darajaSettings;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public B2CResponse processPayout(B2CRequest b2CRequest){

        try {
            Call call = makeB2cRequest(b2CRequest, darajaSettings.getUrl());
            Response response = call.execute();

            if (response.isSuccessful()) {
                String responseBodyString = response.body().string();
                log.info("B2C Response: " + responseBodyString);

                B2CResponse b2CResponse = mapper.readValue(responseBodyString, B2CResponse.class);
                return b2CResponse;
            } else {
                DarajaError errorResponse =
                        mapper.readValue(response.body().string(), DarajaError.class);
                log.info("Error processing B2C request: {}", errorResponse);
                throw new InternalServerException(errorResponse.getErrorMessage());
            }
        } catch (IOException e) {
            throw new InternalServerException("An IO exception occurred while processing payout", e);
        }
    }

    private <T> Call makeB2cRequest(T requestObj, String requestURL) throws IOException {
        String token = "Bearer " + DarajaHttpClient.getB2cSafAccessToken(darajaSettings.getKey(),
                darajaSettings.getSecret(), darajaSettings.getAuthenticationUrl());

        String requestString = mapper.writeValueAsString(requestObj);
        log.info("Request string: {}", requestString);
        RequestBody requestBody = RequestBody.create(JSON, requestString);
        Request request =
                new Request.Builder()
                        .addHeader("Authorization", token)
                        .post(requestBody)
                        .url(requestURL)
                        .build();
        return DarajaHttpClient.getOkHttpClient().newCall(request);
    }
}
