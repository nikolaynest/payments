package payments;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import payments.domain.PaymentRequest;
import payments.domain.model.PaymentResponse;

@RestController("/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    public PaymentResponse createPayment(@RequestBody @Valid PaymentRequest request) {
        return paymentService.createPayment(request);
    }


}
