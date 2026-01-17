package payments.domain.model;

public sealed interface PaymentResult permits PaymentResult.CreatedNew, PaymentResult.AlreadyExist {
    PaymentResponse response();

    record CreatedNew(PaymentResponse response) implements PaymentResult {}

    record AlreadyExist(PaymentResponse response) implements PaymentResult {}

}
