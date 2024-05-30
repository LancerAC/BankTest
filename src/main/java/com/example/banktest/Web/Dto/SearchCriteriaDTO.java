package com.example.banktest.Web.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class SearchCriteriaDTO {

    @NotNull(message = "Введите дату рождения")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotNull(message = "Введите номер телефона")
    private String phoneNumber;

    @NotNull(message = "Введите имя")
    private String firstName;

    @NotNull(message = "Введите фамилию")
    private String middleName;

    @NotNull(message = "Введите отчество")
    private String lastName;

    @Email
    private String email;

}

