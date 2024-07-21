package dev.njari.daraja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author njari_mathenge
 * on 19/07/2024.
 * github.com/iannjari
 */

@SpringBootApplication
@EnableScheduling
public class DarajaB2CApplication {

    public static void main(String[] args) {
        SpringApplication.run(DarajaB2CApplication.class, args);
    }
}
