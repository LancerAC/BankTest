package com.example.banktest.Web.Controller;


import com.example.banktest.Entity.BankAccount;
import com.example.banktest.Entity.User;
import com.example.banktest.Exception.ErrorResponse;
import com.example.banktest.Exception.RequestError;
import com.example.banktest.Configuration.Security.JwtService;
import com.example.banktest.Service.AccountService;
import com.example.banktest.Service.UserService;
import com.example.banktest.Web.Dto.AuthRequestDTO;
import com.example.banktest.Web.Dto.SearchCriteriaDTO;
import com.example.banktest.Web.Dto.TransferRequestDTO;
import com.example.banktest.Web.Dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDTO userDto) {

        try{
            log.info("Запрос на создание пользователя принят");
            BankAccount account = accountService.createAccount(userDto.getBalance());
            userService.createUser(userDto, account);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e){
            String errorMessage = "Невозможно создать запись. " +
                                  "Проверьте правильность ввода всех данных. " +
                                  "Возможно, ник/номер/почта уже заняты: ";
            log.debug("Возникло исключение. Запрос не выполнен");
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchClients( @Validated
           @RequestBody  SearchCriteriaDTO searchCriteria,
            @PageableDefault(size = 10, sort = {"id"},
                    direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            log.info("Запрос на поиск принят");
            List<User> clients = userService.searchClients(searchCriteria, pageable);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            String errorMessage = "Невозможно провести приск." +
                                  " Проверьте заполненные данные и повторите снова";
            log.debug("Возникло иключение. Невозможно выполить поиск");
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

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
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@Validated @RequestBody AuthRequestDTO authRequestDTO) {
        log.info("Запрос на аутентификацию получен");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUserName(),
                        authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequestDTO.getUserName());
        } else {
            log.debug("Возникло иключение");
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
