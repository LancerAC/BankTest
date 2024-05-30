package com.example.banktest.Web.Dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    @NotNull(message = "Введите логин")
    private String userName;

    @NotNull(message = "Введите пароль")
    private String password;
}
