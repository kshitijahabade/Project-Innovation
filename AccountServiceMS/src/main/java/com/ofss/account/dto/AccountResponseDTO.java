package com.ofss.account.dto;

public class AccountResponseDTO {
    private String accountNumber;
    private String message;

    public AccountResponseDTO() {}
    public AccountResponseDTO(String accountNumber, String message) {
        this.accountNumber = accountNumber;
        this.message = message;
    }

    // Getters & Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
