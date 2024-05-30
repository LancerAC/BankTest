package com.example.banktest.Web.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
public class TransferRequestDTO {

    @NotNull(message = "Введите номер телефона, с которого вы хотите перевести деньги")
    private String fromAccountNumber;

    @NotNull(message = "Введите номер телефона, на который вы хотите перевести деньги")
    private String toAccountNumber;

    @NotNull(message = "Введите сумму перевода")
    @Min(value = 1, message = "Введите корректную сумму перевода")
    private Double amount;
}
