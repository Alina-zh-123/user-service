package com.zhilyuk.userservice.specification;

import com.zhilyuk.userservice.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) return null;
            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    public static Specification<User> hasSurname(String surname) {
        return (root, query, criteriaBuilder) -> {
            if (surname == null || surname.isBlank()) return null;
            return criteriaBuilder.equal(root.get("surname"), surname);
        };
    }
}
