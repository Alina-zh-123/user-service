package com.zhilyuk.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name should be up to 255 characters")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    private boolean active;

    private List<PaymentCardDto> paymentCard;
}
