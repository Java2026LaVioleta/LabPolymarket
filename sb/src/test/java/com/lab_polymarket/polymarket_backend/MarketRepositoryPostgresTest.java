/*
package com.lab_polymarket.polymarket_backend;

import com.lab_polymarket.polymarket_backend.model.Market;
import com.lab_polymarket.polymarket_backend.repository.MarketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MarketRepositoryPostgresTest {

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        Properties envProps = loadEnvFile();
        registry.add("spring.datasource.url", () -> envProps.getProperty("DB_URL"));
        registry.add("spring.datasource.username", () -> envProps.getProperty("DB_USERNAME"));
        registry.add("spring.datasource.password", () -> envProps.getProperty("DB_PASSWORD"));
    }

    private static Properties loadEnvFile() {
        Properties props = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    props.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo .env", e);
        }
        return props;
    }

    @Autowired
    private MarketRepository marketRepository;

    @Test
    public void testCreateAndSaveMarketInPostgres() {
        Market market = new Market(
                "market-postgres-123",
                "Will Ethereum reach $5000 in 2026?",
                "cond-789",
                "Crypto",
                "75000",
                LocalDate.of(2026, 12, 31),
                "[\"Yes\", \"No\"]",
                "[\"0.55\", \"0.45\"]",
                "2000000",
                true
        );

        Market savedMarket = marketRepository.save(market);

        assertThat(savedMarket).isNotNull();
        assertThat(savedMarket.getId()).isEqualTo("market-postgres-123");
        assertThat(savedMarket.getQuestion()).isEqualTo("Will Ethereum reach $5000 in 2026?");
        assertThat(savedMarket.getCategory()).isEqualTo("Crypto");
        assertThat(savedMarket.getActive()).isTrue();

        Market foundMarket = marketRepository.findById("market-postgres-123").orElse(null);
        assertThat(foundMarket).isNotNull();
        assertThat(foundMarket.getQuestion()).isEqualTo("Will Ethereum reach $5000 in 2026?");
        assertThat(foundMarket.getCategory()).isEqualTo("Crypto");

        System.out.println("✅ Market guardado exitosamente en PostgreSQL:");
        System.out.println(foundMarket);

        marketRepository.deleteById("market-postgres-123");
    }
}
*/
