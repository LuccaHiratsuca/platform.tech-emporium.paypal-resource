package com.insper.store.paypal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaypalRepository extends JpaRepository<PaypalModel, Long> {
    Optional<PaypalModel> findByPaymentId(String paymentId);
}