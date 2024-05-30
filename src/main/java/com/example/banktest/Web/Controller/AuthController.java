package com.example.banktest.Web.Controller;

import com.example.banktest.Configuration.Security.JwtService;
import com.example.banktest.Exception.RequestError;
import com.example.banktest.Web.Dto.AuthRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
