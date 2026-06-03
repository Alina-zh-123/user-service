package com.innowise.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "payment_cards")
@Getter
@Setter
public class PaymentCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String number;
    private String holder;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    private boolean active;
}
