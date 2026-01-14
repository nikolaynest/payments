package payments.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, name = "external_id")
    private String externalId; // idempotency key

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Payment () {}

    public Payment(String externalId, BigDecimal amount) {
        this.amount = amount;
        this.externalId = externalId;
        this.status = PaymentStatus.PENDING;
        this.createdAt = OffsetDateTime.now();
    }
}
