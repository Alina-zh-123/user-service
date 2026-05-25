package com.innowise.userservice.service;

import com.innowise.userservice.dto.PaymentCardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {
    PaymentCardDto createPaymentCard(Long userId, PaymentCardDto paymentCardDto);
    PaymentCardDto getPaymentCardById(Long id);
    List<PaymentCardDto> getAllPaymentCardsByUserId(Long userId);
    Page<PaymentCardDto> getAllPaymentCardsWithFilter(String name, String surname, Pageable pageable);
    PaymentCardDto updatePaymentCard(Long id, PaymentCardDto paymentCardDto);
    void activatePaymentCard(Long id, Boolean activate);
}

