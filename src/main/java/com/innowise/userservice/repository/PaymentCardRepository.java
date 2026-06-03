package com.innowise.userservice.repository;

import com.innowise.userservice.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long>, JpaSpecificationExecutor<PaymentCard> {
    List<PaymentCard> findByUserId(Long userId);

    @Query(value = "SELECT COUNT(card) FROM PaymentCard card WHERE card.user.id=:userId")
    int countPaymentCardsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE PaymentCard card SET card.active=:activate WHERE card.id = :id")
    void activatePaymentCardById(@Param("id") Long id, @Param("activate") Boolean activate);
}
