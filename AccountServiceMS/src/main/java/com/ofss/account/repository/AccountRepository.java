// AccountServiceMS/src/main/java/com/ofss/account/repository/AccountRepository.java
package com.ofss.account.repository;

import com.ofss.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//  Optional<Account> findByAccountNumber(String accountNumber);
  List<Account> findByCustomerId(Long customerId);
}
