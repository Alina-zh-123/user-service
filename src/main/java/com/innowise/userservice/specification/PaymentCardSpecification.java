package com.innowise.userservice.specification;

import com.innowise.userservice.entity.PaymentCard;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {
    public static Specification<PaymentCard> hasUserName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) return null;
            return criteriaBuilder.equal(root.join("user").get("name"), name);
        };
    }

    public static Specification<PaymentCard> hasUserSurname(String surname) {
        return (root, query, criteriaBuilder) -> {
            if (surname == null || surname.isBlank()) return null;
            return criteriaBuilder.equal(root.join("user").get("surname"), surname);
        };
    }
}
