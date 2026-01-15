package payments.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(@NotBlank String externalId, @NotNull BigDecimal amount) {
}
