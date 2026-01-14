package payments.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payments.domain.model.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
