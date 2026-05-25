package com.innowise.userservice.unit;

import com.innowise.userservice.dto.PaymentCardDto;
import com.innowise.userservice.exception.PaymentCardException;
import com.innowise.userservice.mapper.PaymentCardMapper;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.impl.PaymentCardServiceImpl;
import com.innowise.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentCardServiceTest {
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private PaymentCardMapper paymentCardMapper;
    @Mock
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private PaymentCard paymentCard1;
    private PaymentCard paymentCard2;
    private PaymentCardDto paymentCardDto1;
    private PaymentCardDto paymentCardDto2;
    private PaymentCardDto paymentCardDto3;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("Arina");
        user1.setSurname("Maximova");
        user1.setEmail("qwerty@gmail.com");
        user1.setBirthDate(LocalDate.parse("2007-01-14"));
        user1.setActive(true);
        paymentCard1 = new PaymentCard();
        paymentCard2 = new PaymentCard();
        user1.setPaymentCards(new ArrayList<>(Arrays.asList(paymentCard1, paymentCard2)));

        user2 = new User();
        user2.setId(2L);
        user2.setName("Marina");
        user2.setSurname("Aximova");
        user2.setEmail("ytrewq@gmail.com");
        user2.setBirthDate(LocalDate.parse("2007-01-14"));
        user2.setActive(true);
        user2.setPaymentCards(new ArrayList<>());

        paymentCard1.setId(1L);
        paymentCard1.setUser(user1);
        paymentCard1.setNumber("1234567890123456");
        paymentCard1.setHolder("Arinka");
        paymentCard1.setExpirationDate(LocalDate.parse("2027-12-31"));
        paymentCard1.setActive(true);

        paymentCard2.setId(2L);
        paymentCard2.setUser(user2);
        paymentCard2.setNumber("6543210987654321");
        paymentCard2.setHolder("Arinka");
        paymentCard2.setExpirationDate(LocalDate.parse("2029-06-30"));
        paymentCard2.setActive(false);

        paymentCardDto1 = new PaymentCardDto();
        paymentCardDto1.setId(1L);
        paymentCardDto1.setNumber("1234567890123456");
        paymentCardDto1.setHolder("Arinka");
        paymentCardDto1.setExpirationDate(LocalDate.parse("2027-12-31"));
        paymentCardDto1.setActive(true);

        paymentCardDto2 = new PaymentCardDto();
        paymentCardDto2.setId(2L);
        paymentCardDto2.setNumber("6543210987654321");
        paymentCardDto2.setHolder("Arinka");
        paymentCardDto2.setExpirationDate(LocalDate.parse("2029-06-30"));
        paymentCardDto2.setActive(false);

        paymentCardDto3 = new PaymentCardDto();
        paymentCardDto3.setId(1L);
        paymentCardDto3.setNumber("1111111122222222");
        paymentCardDto3.setHolder("Marinka");
        paymentCardDto3.setExpirationDate(LocalDate.parse("2030-03-30"));
        paymentCardDto3.setActive(false);
    }

    @Test
    void createPayment_shouldCreatePaymentCard() {
        when(paymentCardMapper.dtoToPaymentCard(paymentCardDto1)).thenReturn(paymentCard1);
        when(paymentCardMapper.paymentCardToDto(paymentCard1)).thenReturn(paymentCardDto1);
        when(paymentCardRepository.save(paymentCard1)).thenReturn(paymentCard1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        PaymentCardDto paymentCardDto = paymentCardService.createPaymentCard(1L, paymentCardDto1);
        assertEquals(paymentCardDto, paymentCardDto1);

        verify(paymentCardMapper).dtoToPaymentCard(paymentCardDto1);
        verify(paymentCardMapper).paymentCardToDto(paymentCard1);
        verify(paymentCardRepository).save(paymentCard1);
    }

    @Test
    void getPaymentCardById_shouldReturnPaymentCard() {
        when(paymentCardRepository.findById(1L)).thenReturn(Optional.of(paymentCard1));
        when(paymentCardMapper.paymentCardToDto(paymentCard1)).thenReturn(paymentCardDto1);

        PaymentCardDto paymentCardDto = paymentCardService.getPaymentCardById(1L);
        assertEquals(paymentCardDto1, paymentCardDto);

        verify(paymentCardRepository).findById(1L);
        verify(paymentCardMapper).paymentCardToDto(paymentCard1);
    }

    @Test
    void getPaymentCardById_shouldThrowPaymentCardException() {
        when(paymentCardRepository.findById(1234L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PaymentCardException.class, () -> {
            paymentCardService.getPaymentCardById(1234L);
        });
        assertEquals("PaymentCard is not found!", exception.getMessage());

        verify(paymentCardRepository).findById(1234L);
    }

    @Test
    void getAllPaymentCardsByUserId_shouldReturnAllPaymentCards() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(paymentCardMapper.paymentCardToDto(paymentCard1)).thenReturn(paymentCardDto1);
        when(paymentCardMapper.paymentCardToDto(paymentCard2)).thenReturn(paymentCardDto2);

        List<PaymentCardDto> paymentCardDtos = paymentCardService.getAllPaymentCardsByUserId(1L);
        assertEquals(user1.getPaymentCards().size(), paymentCardDtos.size());
        assertEquals(paymentCardDto1, paymentCardDtos.get(0));
        assertEquals(paymentCardDto2, paymentCardDtos.get(1));

        verify(userRepository).findById(1L);
        verify(paymentCardMapper).paymentCardToDto(paymentCard1);
        verify(paymentCardMapper).paymentCardToDto(paymentCard2);
    }

    @Test
    void updatePaymentCard_shouldUpdatePaymentCard() {
        when(paymentCardRepository.findById(1L)).thenReturn(Optional.of(paymentCard1));
        when(paymentCardRepository.save(paymentCard1)).thenReturn(paymentCard1);
        when(paymentCardMapper.paymentCardToDto(paymentCard1)).thenReturn(paymentCardDto3);

        PaymentCardDto newPaymentCardDto = new PaymentCardDto();
        newPaymentCardDto.setHolder("Marinka");
        newPaymentCardDto.setExpirationDate(LocalDate.parse("2029-03-30"));

        PaymentCardDto paymentCardDto = paymentCardService.updatePaymentCard(1L, newPaymentCardDto);
        assertEquals(paymentCardDto3, paymentCardDto);

        verify(paymentCardRepository).findById(1L);
        verify(paymentCardRepository).save(paymentCard1);
        verify(paymentCardMapper).paymentCardToDto(paymentCard1);
    }

    @Test
    void activatePaymentCard() {
        when(paymentCardRepository.findById(2L)).thenReturn(Optional.of(paymentCard2));
        when(paymentCardRepository.save(paymentCard2)).thenReturn(paymentCard2);

        paymentCardService.activatePaymentCard(2L, true);
        assertTrue(paymentCard2.isActive());

        verify(paymentCardRepository).findById(2L);
        verify(paymentCardRepository).save(paymentCard2);
    }

    @Test
    void deactivatePaymentCard() {
        when(paymentCardRepository.findById(1L)).thenReturn(Optional.of(paymentCard1));
        when(paymentCardRepository.save(paymentCard1)).thenReturn(paymentCard1);

        paymentCardService.activatePaymentCard(1L, false);
        assertFalse(paymentCard1.isActive());

        verify(paymentCardRepository).findById(1L);
        verify(paymentCardRepository).save(paymentCard1);
    }
}
