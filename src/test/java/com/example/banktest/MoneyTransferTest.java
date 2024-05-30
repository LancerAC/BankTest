package com.example.banktest;

import com.example.banktest.Entity.BankAccount;
import com.example.banktest.Entity.User;
import com.example.banktest.Repository.AccountRepository;
import com.example.banktest.Repository.UserRepository;
import com.example.banktest.Service.AccountService;
import com.example.banktest.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Transient;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
@Transactional
@Testcontainers
public class MoneyTransferTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:9.6.12"));


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Test
    void shouldReturnTheResultOfVerifyingTheCorrectnessOfTheTransaction() {

        final double AMOUNT = 300;

        Date date1 = Date.valueOf(LocalDate.now());

        Date date2 = Date.valueOf(LocalDate.now().plusDays(1));

        BankAccount account1 = new BankAccount(99L, 1000.0, 1000.0);
        BankAccount account2 = new BankAccount(100L, 500.0, 500.0);

        User user1 = new User(99L, "User1", "password1",
                "First1", "Middle1", "Last1", date1,
                "email1@example.com", "1234567890", account1);

        User user2 = new User(100L, "User2", "password2",
                "First2", "Middle2", "Last2", date2,
                "email2@example.com", "0987654321", account2);

        userRepository.save(user1);
        userRepository.save(user2);

        accountService.transferMoney(user1.getPhoneNumber(), user2.getPhoneNumber(), AMOUNT);

        List<BankAccount> accounts = accountRepository.findAll();

        double updatedBalance1 = accounts.get(0).getCurrentBalance();
        double updatedBalance2 = accounts.get(1).getCurrentBalance();

        Assertions.assertEquals(account1.getInitialBalance() - 300, updatedBalance1, 0.01);
        Assertions.assertEquals(account2.getInitialBalance() + 300, updatedBalance2, 0.01);
    }


    @Test
    void shouldThrowException() {
        final double AMOUNT = 300;

        Date date1 = Date.valueOf(LocalDate.now());

        Date date2 = Date.valueOf(LocalDate.now().plusDays(1));

        BankAccount fromAccount = new BankAccount(99L, 100, 100);
        BankAccount toAccount = new BankAccount(100L, 500.0, 500);

        User fromUser = new User(99L, "User1", "password1",
                "First1", "Middle1", "Last1", date1,
                "email1@example.com", "1234567890", fromAccount);

        User toUser = new User(100L, "User2", "password2",
                "First2", "Middle2", "Last2", date2,
                "email2@example.com", "0987654321", toAccount);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        Assertions.assertThrows(RuntimeException.class, () ->
                accountService.transferMoney(
                        fromUser.getPhoneNumber(),
                        toUser.getPhoneNumber(),
                        AMOUNT));
    }


}

