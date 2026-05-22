package com.zhilyuk.userservice.service;

import com.zhilyuk.userservice.dto.PaymentCardDto;

import java.time.LocalDate;
import java.util.List;

public interface PaymentCardService {
    public PaymentCardDto createPaymentCard(Long userId, PaymentCardDto paymentCardDto);
    public PaymentCardDto getPaymentCardById(Long id);
    public List<PaymentCardDto> getAllPaymentCardsByUserId(Long userId);
    public PaymentCardDto updatePaymentCard(Long id, String holder, LocalDate expiration_date);
    public void activatePaymentCard(Long id, Boolean activate);
}

