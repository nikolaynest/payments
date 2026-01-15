package payments.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payments.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByExternalId(String externalId);


}
