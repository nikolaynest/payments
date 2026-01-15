package payments.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String externalId) {
        super(String.format("Payment not found for externalId [%s]", externalId));
    }
}
