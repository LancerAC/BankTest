package com.example.banktest.Web.Controller;


import com.example.banktest.Configuration.Security.JwtService;
import com.example.banktest.Exception.ErrorResponse;
import com.example.banktest.Exception.RequestError;
import com.example.banktest.Service.AccountService;
import com.example.banktest.Service.UserService;
import com.example.banktest.Web.Dto.TransferRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Validated @RequestBody TransferRequestDTO request) {
        try {
            log.info("Запрос на перевод принят");
            accountService.transferMoney(request.getFromAccountNumber(),
                    request.getToAccountNumber(),
                    request.getAmount());
        } catch (Exception e) {
            String errorMessage = "Невозможно провести транзакцию." +
                                  " Проверьте заполненные данные и повторите снова";
            log.debug("Возникло исключение. Невозможно выполнить перевод денег");
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Деньги переведены успешно");
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<RequestError> handleExceptions(BindException ex) {
        RequestError requestError = new RequestError(System.currentTimeMillis(),
                ex.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(requestError,
                HttpStatus.BAD_REQUEST);
    }
}
