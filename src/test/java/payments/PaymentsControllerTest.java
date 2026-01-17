package payments;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import payments.domain.model.PaymentResponse;
import payments.domain.model.PaymentResult;
import payments.domain.model.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//@WebMvcTest(PaymentsController.class) TODO: Seems, like springboot 4.0.1 is not fully working with WebMvcTests

/**
 * Tests for web-layer only
 */
public class PaymentsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PaymentService paymentService;

    @Test @Disabled
    void createPayment_returns201() throws Exception {
        when(paymentService.createPayment(any()))
                .thenReturn(new PaymentResult.CreatedNew(
                        new PaymentResponse(UUID.randomUUID(),  "order-1", PaymentStatus.PENDING, BigDecimal.TEN)
                ));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "externalId": "order-1",
                              "amount": 10
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.externalId").value("order-1"));
    }


    @Test @Disabled
    void createPayment_returns204_whenAlreadyExists() throws Exception {
        when(paymentService.createPayment(any()))
                .thenReturn(new PaymentResult.AlreadyExist(null));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "externalId": "order-1",
                              "amount": 10
                            }
                            """))
                .andExpect(status().isNoContent());
    }
}
