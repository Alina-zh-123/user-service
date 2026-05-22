package com.zhilyuk.userservice.controller;

import com.zhilyuk.userservice.dto.PaymentCardDto;
import com.zhilyuk.userservice.service.PaymentCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/paymentCards")
@RequiredArgsConstructor
public class PaymentCardController {
    private final PaymentCardService paymentCardService;

    @PostMapping
    public ResponseEntity<PaymentCardDto> createPaymentCard(@RequestParam Long userId, @Valid @RequestBody PaymentCardDto paymentCardDto) {
        return new ResponseEntity<>(paymentCardService.createPaymentCard(userId, paymentCardDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardDto> getPaymentCardById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentCardService.getPaymentCardById(id), HttpStatus.OK);
    }

    @GetMapping("/byUserId")
    public ResponseEntity<List<PaymentCardDto>> getAllPaymentCardsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(paymentCardService.getAllPaymentCardsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentCardDto> updatePaymentCard(@PathVariable Long id, @RequestParam String holder, @RequestParam LocalDate expirationDate) {
        return new ResponseEntity<>(paymentCardService.updatePaymentCard(id, holder, expirationDate), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> activatePaymentCard(@PathVariable Long id, @RequestParam Boolean activate) {
        paymentCardService.activatePaymentCard(id, activate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
