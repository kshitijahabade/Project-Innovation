package com.ofss.bank.dto;

import jakarta.validation.constraints.NotBlank;

public class BankRequestDTO {

    @NotBlank(message = "Bank name is required")
    private String name;

    @NotBlank(message = "IFSC Code is required")
    private String ifscCode;

    @NotBlank(message = "Address is required")
    private String address;

    public BankRequestDTO() {}

    public BankRequestDTO(String name, String ifscCode, String address) {
        this.name = name;
        this.ifscCode = ifscCode;
        this.address = address;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
