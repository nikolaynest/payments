package payments;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payments.domain.PaymentRequest;
import payments.domain.model.PaymentResponse;
import payments.domain.model.PaymentResult;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid PaymentRequest request) {
        PaymentResult result = paymentService.createPayment(request);
        if (result instanceof PaymentResult.CreatedNew) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result.response());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<PaymentResponse> readPayment(@PathVariable String externalId) {
        return ResponseEntity.ok(paymentService.getPayment(externalId));
    }
}
