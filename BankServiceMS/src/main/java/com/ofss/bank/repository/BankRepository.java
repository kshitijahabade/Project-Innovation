package com.ofss.bank.repository;

import com.ofss.bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    boolean existsByIfscCode(String ifscCode);
}
