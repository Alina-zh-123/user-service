package com.innowise.userservice.service;

import com.innowise.userservice.dto.PaymentCardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {
    public PaymentCardDto createPaymentCard(Long userId, PaymentCardDto paymentCardDto);
    public PaymentCardDto getPaymentCardById(Long id);
    public List<PaymentCardDto> getAllPaymentCardsByUserId(Long userId);
    public Page<PaymentCardDto> getAllPaymentCardsWithFilter(String name, String surname, Pageable pageable);
    public PaymentCardDto updatePaymentCard(Long id, PaymentCardDto paymentCardDto);
    public void activatePaymentCard(Long id, Boolean activate);
}

