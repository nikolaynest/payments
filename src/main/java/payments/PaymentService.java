package payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payments.domain.PaymentRequest;
import payments.domain.model.Payment;
import payments.domain.model.PaymentResponse;
import payments.domain.model.PaymentResult;
import payments.domain.repository.PaymentRepository;
import payments.exceptions.PaymentNotFoundException;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    @Transactional
    public PaymentResult createPayment(PaymentRequest request) {
        return paymentRepository.findByExternalId(request.externalId())
                .<PaymentResult>map(payment -> new PaymentResult.AlreadyExist(PaymentResponse.from(payment)))
                .orElseGet(() -> new PaymentResult.CreatedNew(PaymentResponse.from(
                        paymentRepository.save(new Payment(request.externalId(), request.amount())))));
    }

    public PaymentResponse getPayment(String externalId) {
        return PaymentResponse.from(
                paymentRepository.findByExternalId(externalId)
                        .orElseThrow(() -> new PaymentNotFoundException(externalId))
        );
    }
}
