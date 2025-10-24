package com.ofss.bank.dto;

public class BankResponseDTO {

    private String bankId;
    private String message;

    public BankResponseDTO() {}
    public BankResponseDTO(String bankId, String message) {
        this.bankId = bankId;
        this.message = message;
    }

    // Getters and setters
    public String getBankId() { return bankId; }
    public void setBankId(String bankId) { this.bankId = bankId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
