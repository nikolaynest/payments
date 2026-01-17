package payments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import payments.domain.PaymentRequest;
import payments.domain.model.Payment;
import payments.domain.model.PaymentResponse;
import payments.domain.model.PaymentResult;
import payments.domain.repository.PaymentRepository;
import payments.exceptions.PaymentNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository repository;

    @InjectMocks
    PaymentService service;

    @Test
    void createPayment_createsNewPayment() {
        // Arrange
        PaymentRequest request =
                new PaymentRequest("order-1", BigDecimal.TEN);

        when(repository.findByExternalId("order-1"))
                .thenReturn(Optional.empty());

        when(repository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        PaymentResult result = service.createPayment(request);

        // Assert
        assertTrue(result instanceof PaymentResult.CreatedNew);
        assertEquals("order-1", result.response().externalId());
    }

    @Test
    void createPayment_returnsAlreadyExists() {
        // Arrange
        when(repository.findByExternalId("order-1"))
                .thenReturn(Optional.of(new Payment("order-1", BigDecimal.TEN)));

        // Act
        PaymentResult result = service.createPayment(
                new PaymentRequest("order-1", BigDecimal.TEN)
        );

        // Assert
        assertTrue(result instanceof PaymentResult.AlreadyExist);
        verify(repository, never()).save(any());
    }

    @Test
    void getPayment_shouldReturnPaymentResponse_whenPaymentExists() {
        // Arrange
        String externalId = "order-123";
        Payment payment = new Payment(externalId, BigDecimal.TEN);
        when(repository.findByExternalId(externalId)).thenReturn(Optional.of(payment));

        // Act
        PaymentResponse response = service.getPayment(externalId);

        // Assert
        assertNotNull(response);
        assertEquals(externalId, response.externalId());
        verify(repository, times(1)).findByExternalId(externalId);
    }

    @Test
    void getPayment_shouldThrowException_whenPaymentDoesNotExist() {
        String externalId = "non-existent";
        when(repository.findByExternalId(externalId)).thenReturn(Optional.empty());

        PaymentNotFoundException ex = assertThrows(PaymentNotFoundException.class, () ->
                service.getPayment(externalId)
        );

        assertEquals("Payment not found for externalId [non-existent]", ex.getMessage());
        verify(repository, times(1)).findByExternalId(externalId);
    }
}
