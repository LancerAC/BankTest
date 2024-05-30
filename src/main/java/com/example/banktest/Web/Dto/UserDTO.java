package com.example.banktest.Web.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {


    @NotNull
    private String  phoneNumber;

    @NotNull(message = "Введите никнейм")
    private String userName;

    @NotNull(message = "Введите пароль")
    private String password;

    @Email
    private String email;

    @NotNull(message = "Введите имя")
    private String firstName;

    @NotNull(message = "Введите фамилию")
    private String middleName;

    @NotNull(message = "Введите отчество")
    private String lastName;

    @NotNull(message = "Введите дату в формате: yyyy-MM-dd")
    private String birthDate;

    @Min(value = 0, message = "Ваш баланс не может быть отрицательным")
    private Double balance;

}
