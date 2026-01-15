package payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payments.domain.PaymentRequest;
import payments.domain.model.Payment;
import payments.domain.model.PaymentResponse;
import payments.domain.repository.PaymentRepository;
import payments.exceptions.PaymentNotFoundException;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = paymentRepository.findByExternalId(request.externalId())
                .orElseGet(() -> paymentRepository.save(new Payment(request.externalId(), request.amount())));

        return PaymentResponse.from(payment);
    }

    public PaymentResponse getPayment(String externalId) {
        return PaymentResponse.from(
                paymentRepository.findByExternalId(externalId)
                        .orElseThrow(() -> new PaymentNotFoundException(externalId))
        );
    }
}
