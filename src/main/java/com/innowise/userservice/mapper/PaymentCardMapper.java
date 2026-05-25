package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.PaymentCardDto;
import com.innowise.userservice.entity.PaymentCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {
    public PaymentCard dtoToPaymentCard(PaymentCardDto paymentCardDto);
    public PaymentCardDto paymentCardToDto(PaymentCard paymentCard);
}
