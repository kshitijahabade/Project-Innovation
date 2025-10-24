package com.ofss.bank.dto;

public class BankViewDTO {

    private Long id;
    private String name;
    private String ifscCode;
    private String address;

    public BankViewDTO() {}
    public BankViewDTO(Long id, String name, String ifscCode, String address) {
        this.id = id;
        this.name = name;
        this.ifscCode = ifscCode;
        this.address = address;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
