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


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;


    private String phoneNumber;


    private String firstName;


    private String middleName;


    private String lastName;

    @Email
    private String email;

}

