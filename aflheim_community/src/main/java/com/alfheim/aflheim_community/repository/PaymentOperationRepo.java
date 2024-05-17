package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.payment.PaymentOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentOperationRepo extends JpaRepository<PaymentOperation, Long> {

    Optional<PaymentOperation> findByChargeId(String chargeId);
}
