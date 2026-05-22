package com.zhilyuk.userservice.service.impl;

import com.zhilyuk.userservice.dto.PaymentCardDto;
import com.zhilyuk.userservice.exception.PaymentCardException;
import com.zhilyuk.userservice.exception.UserException;
import com.zhilyuk.userservice.mapper.PaymentCardMapper;
import com.zhilyuk.userservice.model.PaymentCard;
import com.zhilyuk.userservice.model.User;
import com.zhilyuk.userservice.repository.PaymentCardRepository;
import com.zhilyuk.userservice.repository.UserRepository;
import com.zhilyuk.userservice.service.PaymentCardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {
    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;
    private final UserRepository userRepository;

    @Override
    @CachePut(value = "paymentCardCache", key = "#result.id")
    public PaymentCardDto createPaymentCard(Long userId, PaymentCardDto paymentCardDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User is not found!"));
        if (user.getPaymentCards().size() > 5) {
            throw new PaymentCardException("User cannot have more than 5 cards");
        }
        PaymentCard paymentCard = paymentCardMapper.dtoToPaymentCard(paymentCardDto);
        paymentCard.setUser(user);

        PaymentCard res = paymentCardRepository.save(paymentCard);
        return paymentCardMapper.paymentCardToDto(res);
    }

    @Override
    @Cacheable(value = "paymentCardCache", key = "#id")
    public PaymentCardDto getPaymentCardById(Long id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardException("PaymentCard is not found!"));
        return paymentCardMapper.paymentCardToDto(paymentCard);
    }

    @Override
    public List<PaymentCardDto> getAllPaymentCardsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User is not found!"));
        List<PaymentCard> res = user.getPaymentCards();
        return res.stream()
                .map(paymentCardMapper::paymentCardToDto)
                .toList();
    }

    @Override
    @Transactional
    @CachePut(value = "paymentCardCache", key = "#id")
    public PaymentCardDto updatePaymentCard(Long id, String holder, LocalDate expirationDate) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardException("PaymentCard is not found!"));
        paymentCard.setHolder(holder);
        paymentCard.setExpirationDate(expirationDate);
        paymentCardRepository.save(paymentCard);
        return paymentCardMapper.paymentCardToDto(paymentCard);
    }

    @Override
    @Transactional
    @CacheEvict(value = "paymentCardCache", key = "#id")
    public void activatePaymentCard(Long id, Boolean activate) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardException("PaymentCard is not found!"));
        paymentCard.setActive(activate);
        paymentCardRepository.save(paymentCard);
    }
}
