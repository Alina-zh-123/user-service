package com.zhilyuk.userservice.repository;

import com.zhilyuk.userservice.model.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long>, JpaSpecificationExecutor<PaymentCard> {
    List<PaymentCard> findByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE payment_cards SET active=true WHERE id=:id", nativeQuery = true)
    void activatePaymentCardById(@Param("id") Long id);
    @Modifying
    @Query(value = "UPDATE payment_cards SET active=false WHERE id=:id", nativeQuery = true)
    void deactivatePaymentCardById(@Param("id") Long id);
}
