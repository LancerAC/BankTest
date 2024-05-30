package com.example.banktest.Service;

import com.example.banktest.Entity.BankAccount;
import com.example.banktest.Repository.AccountRepository;
import com.example.banktest.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public BankAccount createAccount(Double moneyAmount) {
        BankAccount account = BankAccount.builder()
                .currentBalance(moneyAmount)
                .initialBalance(moneyAmount)
                .build();

        log.info("Аккаунт создан");
        return accountRepository.save(account);
    }

    @Scheduled(fixedDelay = 60000)
    public void updateClientBalances() {

        List<BankAccount> bankAccounts = accountRepository.findAll();

        boolean allAtMaxBalance = bankAccounts.stream()
                .allMatch(ba -> ba.getCurrentBalance() >= ba.getInitialBalance() * 2.07);

        if (allAtMaxBalance) {
            log.info("Баланс всех пользователей достиг максимума");
            return;
        }

        for (BankAccount bankAccount : bankAccounts) {

            double newBalance = bankAccount.getCurrentBalance() * 1.05;

            if (newBalance <= bankAccount.getInitialBalance() * 2.07) {
                bankAccount.setCurrentBalance(newBalance);
                accountRepository.save(bankAccount);

                log.debug("Баланс клиента обновлен");
            }
        }
    }

    public void transferMoney(String from, String to, Double amount) {
        BankAccount fromAccount = userRepository.findByPhoneNumber(from).getBankAccount();
        BankAccount toAccount = userRepository.findByPhoneNumber(to).getBankAccount();

        if (fromAccount.getCurrentBalance() < amount) {
            log.debug("Невозможно перевести деньги");
            throw new RuntimeException();

        }

        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance() - amount);
        toAccount.setCurrentBalance(toAccount.getCurrentBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Деньги переведены успешно");
    }

}
