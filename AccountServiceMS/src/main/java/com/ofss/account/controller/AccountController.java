package com.ofss.account.controller;

import com.ofss.account.dto.AccountRequestDTO;
import com.ofss.account.dto.AccountResponseDTO;
import com.ofss.account.entity.Account;
import com.ofss.account.repository.AccountRepository;
import com.ofss.account.feign.CustomerClient;
import com.ofss.account.feign.KycClient;
import com.ofss.account.feign.BankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private BankClient bankClient;
    
    @Autowired
    private KycClient kycClient;


    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO request) {
        Map<String, Object> kycData = null;

        try {
            kycData = kycClient.getKyc(request.getCustomerId());
        } catch (Exception ex) {
            // Handles 404 or Feign exception
            return ResponseEntity.badRequest().body(
                new AccountResponseDTO(null, 
                    "No KYC record found for the customer. Please complete your KYC before creating an account.")
            );
        }

        // If KYC exists but not verified
        if (kycData == null || !"VERIFIED".equalsIgnoreCase((String) kycData.get("status"))) {
            return ResponseEntity.badRequest().body(
                new AccountResponseDTO(null, 
                    "KYC incomplete or not verified. Please complete verification before creating an account.")
            );
        }

        try {
            Object customer = customerClient.getCustomerById(request.getCustomerId());
            Object bank = bankClient.getBankById(request.getBankId());

            if (customer == null || bank == null) {
                return ResponseEntity.badRequest().body(
                    new AccountResponseDTO(null, "Invalid customerId or bankId")
                );
            }

            // Auto-generate account number in entity using @PrePersist or UUID
            Account account = new Account(
                request.getAccountType(),
                request.getAccountStatus(),
                request.getBalance(),
                request.getCustomerId(),
                request.getBankId()
            );

            accountRepository.save(account);

            return ResponseEntity.ok(
                new AccountResponseDTO(account.getAccountNumber(), "Account created successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new AccountResponseDTO(null, "Error creating account: " + e.getMessage())
            );
        }
    }



    // ✅ Get all accounts
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    // ✅ Get Account by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null)
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Account not found with ID: " + id));

        return ResponseEntity.ok(account);
    }

    // ✅ Update Account
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDTO request) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null)
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Account not found with ID: " + id));

        account.setAccountType(request.getAccountType());
        account.setAccountStatus(request.getAccountStatus());
        account.setBalance(request.getBalance());
        account.setCustomerId(request.getCustomerId());
        account.setBankId(request.getBankId());

        accountRepository.save(account);
        return ResponseEntity.ok(new AccountResponseDTO("ACC-" + id, "Account updated successfully"));
    }

    // ✅ Partial Update
    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> patchAccount(@PathVariable Long id, @RequestBody AccountRequestDTO request) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null)
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Account not found with ID: " + id));

        if (request.getAccountType() != null) account.setAccountType(request.getAccountType());
        if (request.getAccountStatus() != null) account.setAccountStatus(request.getAccountStatus());
        if (request.getBalance() != null) account.setBalance(request.getBalance());

        accountRepository.save(account);
        return ResponseEntity.ok(new AccountResponseDTO("ACC-" + id, "Account partially updated"));
    }

    // ✅ Delete Account
    @DeleteMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> deleteAccount(@PathVariable Long id) {
        if (!accountRepository.existsById(id))
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Account not found"));

        accountRepository.deleteById(id);
        return ResponseEntity.ok(new AccountResponseDTO("ACC-" + id, "Account deleted successfully"));
    }

    // ✅ Deposit
    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@PathVariable Long id, @RequestParam double amount) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null)
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Account not found"));

        if (amount <= 0)
            return ResponseEntity.badRequest().body(new AccountResponseDTO("ACC-" + id, "Invalid amount"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        return ResponseEntity.ok(new AccountResponseDTO("ACC-" + id, "Deposited ₹" + amount + " successfully"));
    }

    // ✅ Withdraw
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponseDTO> withdraw(@PathVariable Long id, @RequestParam double amount) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null)
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Account not found"));

        if (amount <= 0)
            return ResponseEntity.badRequest().body(new AccountResponseDTO("ACC-" + id, "Invalid amount"));
        if (account.getBalance() < amount)
            return ResponseEntity.badRequest().body(new AccountResponseDTO("ACC-" + id, "Insufficient balance"));

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return ResponseEntity.ok(new AccountResponseDTO("ACC-" + id, "Withdrawn ₹" + amount + " successfully"));
    }

    // ✅ Transfer
    @PostMapping("/{sourceId}/transfer/{targetId}")
    public ResponseEntity<AccountResponseDTO> transfer(@PathVariable Long sourceId, @PathVariable Long targetId, @RequestParam double amount) {
        if (sourceId.equals(targetId))
            return ResponseEntity.badRequest().body(new AccountResponseDTO(null, "Cannot transfer to same account"));

        Account source = accountRepository.findById(sourceId).orElse(null);
        Account target = accountRepository.findById(targetId).orElse(null);
        if (source == null || target == null)
            return ResponseEntity.status(404).body(new AccountResponseDTO(null, "Source or target account not found"));

        if (amount <= 0)
            return ResponseEntity.badRequest().body(new AccountResponseDTO(null, "Invalid transfer amount"));
        if (source.getBalance() < amount)
            return ResponseEntity.badRequest().body(new AccountResponseDTO(null, "Insufficient balance"));

        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
        accountRepository.save(source);
        accountRepository.save(target);

        return ResponseEntity.ok(new AccountResponseDTO("ACC-" + sourceId, "Transferred ₹" + amount + " from Account " + sourceId + " to " + targetId));
    }
}
