package com.ofss.account.entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ACCOUNT_NUMBER", unique = true, nullable = false)
    private String accountNumber;
    private String accountType;
    private String accountStatus;
    private Double balance;
    private Long customerId;
    private Long bankId;

    public Account() {}

    public Account(String accountType, String accountStatus, Double balance, Long customerId, Long bankId) {
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.balance = balance;
        this.customerId = customerId;
        this.bankId = bankId;
        this.accountNumber = generateMockAccountNumber();
    }
    
    private String generateMockAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }
}
