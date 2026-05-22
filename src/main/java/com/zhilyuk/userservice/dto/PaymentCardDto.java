package com.zhilyuk.userservice.dto;

import com.zhilyuk.userservice.model.User;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentCardDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Number cannot be blank")
    @Size(min = 16, max = 16, message = "Payment card number should be 16 characters")
    private String number;

    @NotBlank(message = "Holder cannot be blank")
    private String holder;

    @FutureOrPresent(message = "Expiration date should be in the future or present")
    private LocalDate expirationDate;
    private boolean active;
}
