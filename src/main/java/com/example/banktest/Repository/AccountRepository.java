package com.example.banktest.Repository;

import com.example.banktest.Entity.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
}
