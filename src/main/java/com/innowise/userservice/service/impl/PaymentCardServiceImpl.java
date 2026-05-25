package com.innowise.userservice.service.impl;

import com.innowise.userservice.dto.PaymentCardDto;
import com.innowise.userservice.exception.PaymentCardException;
import com.innowise.userservice.exception.UserException;
import com.innowise.userservice.mapper.PaymentCardMapper;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.PaymentCardService;
import com.innowise.userservice.specification.PaymentCardSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {
    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Override
    @CachePut(value = "paymentCardCache", key = "#result.id")
    @CacheEvict(value = "userCache", key = "#userId")
    public PaymentCardDto createPaymentCard(Long userId, PaymentCardDto paymentCardDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User is not found!"));

        if (paymentCardRepository.countPaymentCardsByUserId(userId) >= 5) {
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
        List<PaymentCard> res = paymentCardRepository.findByUserId(userId);
        return res.stream()
                .map(paymentCardMapper::paymentCardToDto)
                .toList();
    }

    @Override
    public Page<PaymentCardDto> getAllPaymentCardsWithFilter(String name, String surname, Pageable pageable) {
        Specification<PaymentCard> spec = Specification
                .where(PaymentCardSpecification.hasUserName(name))
                .and(PaymentCardSpecification.hasUserSurname(surname));
        Page<PaymentCard> res = paymentCardRepository.findAll(spec, pageable);
        return res.map(paymentCardMapper::paymentCardToDto);
    }

    @Override
    @Transactional
    @CachePut(value = "paymentCardCache", key = "#id")
    public PaymentCardDto updatePaymentCard(Long id, PaymentCardDto paymentCardDto) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardException("PaymentCard is not found!"));

        paymentCard.setHolder(paymentCardDto.getHolder());
        paymentCard.setExpirationDate(paymentCardDto.getExpirationDate());
        paymentCardRepository.save(paymentCard);

        cacheManager.getCache("userCache").evict(paymentCard.getUser().getId());
        return paymentCardMapper.paymentCardToDto(paymentCard);
    }

    @Override
    @Transactional
    @CacheEvict(value = "paymentCardCache", key = "#id")
    public void activatePaymentCard(Long id, Boolean activate) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardException("PaymentCard is not found!"));
        Long userId = paymentCard.getUser().getId();

        paymentCardRepository.activatePaymentCardById(id, activate);
        cacheManager.getCache("userCache").evict(userId);
    }
}
