package com.innowise.userservice.controller;

import com.innowise.userservice.dto.OnActivate;
import com.innowise.userservice.dto.PaymentCardDto;
import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.service.PaymentCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentCardController {
    private final PaymentCardService paymentCardService;

    @PostMapping("/users/{userId}/payment-cards")
    public ResponseEntity<PaymentCardDto> createPaymentCard(@PathVariable Long userId, @Valid @RequestBody PaymentCardDto paymentCardDto) {
        return new ResponseEntity<>(paymentCardService.createPaymentCard(userId, paymentCardDto), HttpStatus.CREATED);
    }

    @GetMapping("/payment-cards/{id}")
    public ResponseEntity<PaymentCardDto> getPaymentCardById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentCardService.getPaymentCardById(id), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/payment-cards")
    public ResponseEntity<List<PaymentCardDto>> getAllPaymentCardsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentCardService.getAllPaymentCardsByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentCardDto>> getAllPaymentCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            Pageable pageable) {
        return new ResponseEntity<>(paymentCardService.getAllPaymentCardsWithFilter(name, surname, pageable), HttpStatus.OK);
    }

    @PutMapping("/payment-cards/{id}")
    public ResponseEntity<PaymentCardDto> updatePaymentCard(@PathVariable Long id, @Valid @RequestBody PaymentCardDto paymentCardDto) {
        return new ResponseEntity<>(paymentCardService.updatePaymentCard(id, paymentCardDto), HttpStatus.OK);
    }

    @PatchMapping("/payment-cards/{id}")
    public ResponseEntity<Void> activatePaymentCard(
            @PathVariable Long id,
            @Validated(OnActivate.class) @RequestBody PaymentCardDto paymentCardDto) {
        paymentCardService.activatePaymentCard(id, paymentCardDto.isActive());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
