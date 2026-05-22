package com.zhilyuk.userservice.mapper;

import com.zhilyuk.userservice.dto.PaymentCardDto;
import com.zhilyuk.userservice.model.PaymentCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {
    public PaymentCard dtoToPaymentCard(PaymentCardDto paymentCardDto);
    public PaymentCardDto paymentCardToDto(PaymentCard paymentCard);
}
