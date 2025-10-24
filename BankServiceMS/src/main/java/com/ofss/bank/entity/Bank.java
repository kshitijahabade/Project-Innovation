package com.ofss.bank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BANK")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String ifscCode;

    private String address;

    public Bank() {}
    public Bank(String name, String ifscCode, String address) {
        this.name = name;
        this.ifscCode = ifscCode;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
