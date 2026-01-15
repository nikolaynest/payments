package payments;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import payments.domain.PaymentRequest;
import payments.domain.model.Payment;
import payments.domain.model.PaymentResponse;
import payments.domain.model.PaymentStatus;
import payments.domain.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class PaymentIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("payments")
            .withUsername("it-test")
            .withPassword("it-test");


    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    PaymentRepository repository;

    @Autowired
    PaymentService service;

    @Test
    void createPayment_shouldPersistInDatabase() {
        PaymentRequest request = new PaymentRequest("order-123", BigDecimal.TEN);

        PaymentResponse response = service.createPayment(request);

        Optional<Payment> saved = repository.findByExternalId("order-123");
        assertTrue(saved.isPresent());
        assertEquals(PaymentStatus.PENDING, saved.get().getStatus());
    }
}
