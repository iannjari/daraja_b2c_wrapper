package dev.njari.daraja.api.b2c.daraja;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.njari.daraja.api.b2c.daraja.dto.DarajaError;
import dev.njari.daraja.api.b2c.daraja.dto.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */

@Slf4j
@RequiredArgsConstructor
public class DarajaHttpClient {


    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);


    /**
     * controls how a network client should behave after a request
     *
     * @return - OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder builder =
                new OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .writeTimeout(1, TimeUnit.MINUTES)
                        .addInterceptor(logging)
                        .cache(null);

        return builder.build();
    }

    /**
     * Get Base64 Encoded string
     *
     * @param consumerKey
     * @param consumerSecret
     * @return
     */
    public static String getBase64(String consumerKey, String consumerSecret) {
        String appKeySecret = consumerKey + ":" + consumerSecret;
        byte[] bytes = new byte[0];

        try {
            bytes = appKeySecret.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * encode password using shortcode, passkey, and timestamp
     *
     * @param shortCode
     * @param passkey
     * @param timestamp
     * @return
     */
    public static String getEncodedPassword(String shortCode, String passkey, String timestamp) {
        String appKeySecret = shortCode + passkey + timestamp;
        byte[] bytes = appKeySecret.getBytes(StandardCharsets.ISO_8859_1);

        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * generate daraja B2C security credentials using the public cert
     *
     * @param b2cPassword
     * @param certPath
     * @param resourceLoader
     * @return
     */
    public static String generateSecurityCredentials(
            String b2cPassword, String certPath, ResourceLoader resourceLoader) {
        try {
            // Load the certificate file as a resource from the classpath
            Resource resource = resourceLoader.getResource(certPath);

            // Read the contents of the certificate file
            byte[] certBytes = StreamUtils.copyToByteArray(resource.getInputStream());

            // Create a certificate object from the certificate bytes
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certBytes));

            // Extract the public key from the certificate
            RSAPublicKey rsaPublicKey = (RSAPublicKey) cert.getPublicKey();

            // Encrypt the password using the public key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            byte[] encryptedPayload = cipher.doFinal(b2cPassword.getBytes(StandardCharsets.UTF_8));

            // Encode the encrypted payload as Base64 and return
            return Base64.getEncoder().encodeToString(encryptedPayload);
        } catch (IOException
                 | CertificateException
                 | NoSuchAlgorithmException
                 | NoSuchPaddingException
                 | InvalidKeyException
                 | IllegalBlockSizeException
                 | BadPaddingException e) {
            throw new RuntimeException("Error generating security credentials", e);
        }
    }

    public static String getB2cSafAccessToken(String key, String secret, String authUrl)
            throws IOException {

        var basic = "Basic ".concat(getBase64(key, secret));

        OkHttpClient client = new OkHttpClient();
        Request request =
                new Request.Builder()
                        .get()
                        .url(authUrl)
                        .addHeader("Authorization", basic)
                        .addHeader("Content-Type", "application/json")
                        .addHeader(
                                "Cookie",
                                "incap_ses_1020_2742146=L6WcVd/E9QEv5ihNncUnDq+1v2QAAAAAtYnV71L6M03oNT8Xs5eaQw==; visid_incap_2742146=sA8kfhgSTDq9xkTAIMjWC2ikv2QAAAAAQUIPAAAAAAAxuRB8aW20oWwFBTXK5wUt")
                        .build();

        log.info(
                "Request parameters to Safaricom: \nMethod: {} \nURL: {} \nHeaders: {}",
                request.method(),
                request.url(),
                request.headers().toString());
        Call call = client.newCall(request);
        Response response = call.execute();

        ObjectMapper mapper = new ObjectMapper();


        if (response.isSuccessful()) {
            String tokenString = response.body().string();
            log.info("Safaricom Token: {}", tokenString);

            Token token = mapper.readValue(tokenString, Token.class);

            return token.getAccessToken();
        } else {
            log.info(
                    "Mpesa Response \n URL: {} \n Headers: {} \n Body: {}",
                    response.code(),
                    response.headers(),
                    response.body().string());
            DarajaError errorResponse =
                    mapper.readValue(response.body().string(), DarajaError.class);
            log.error(errorResponse.getErrorMessage());

            return getB2cSafAccessToken(key, secret, authUrl); //makes this call recursive in cases of failure
        }
    }
}