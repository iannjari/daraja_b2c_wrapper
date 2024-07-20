package dev.njari.daraja.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("dev.njari.daraja")
@EnableJpaAuditing
public class DatabaseConfiguration {
}
