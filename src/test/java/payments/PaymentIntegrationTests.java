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
import payments.domain.model.PaymentResponse;
import payments.domain.model.PaymentResult;
import payments.domain.model.PaymentStatus;
import payments.domain.repository.PaymentRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class PaymentIntegrationTests {

    @Autowired
    PaymentRepository repository;

    @Autowired
    PaymentService service;

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

    @Test
    void createPayment_shouldPersistInDatabase() {
        String externalId = "order-123";
        PaymentRequest request = new PaymentRequest(externalId, BigDecimal.TEN);

        PaymentResult created = service.createPayment(request);

        PaymentResponse payment = service.getPayment(externalId);

        assertEquals(externalId, created.response().externalId());
        assertEquals(externalId, payment.externalId());
        assertEquals(PaymentStatus.PENDING, payment.status());
    }

    @Test
    void createPayment_shouldBeIdempotent() {
        PaymentRequest request = new PaymentRequest("order-234", BigDecimal.TEN);

        PaymentResult created = service.createPayment(request);
        PaymentResult existed = service.createPayment(request);

        assertEquals(created.response().paymentId(), existed.response().paymentId());
    }
}
