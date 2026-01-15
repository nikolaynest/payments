package payments.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(UUID paymentId, String externalId, PaymentStatus status, BigDecimal amount) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getExternalId(), payment.getStatus(), payment.getAmount());
    }
}
