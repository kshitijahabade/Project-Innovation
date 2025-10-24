package com.ofss.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Date of birth is required")@Pattern(
    		  regexp = "^\\d{4}-\\d{2}-\\d{2}$",
    		  message = "Invalid date format (expected yyyy-MM-dd)"
    		)
    private String dob; 

    @NotBlank(message = "Address is required")
    private String address;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN format")
    private String pan;

    @Pattern(regexp = "^\\d{12}$", message = "Invalid Aadhaar number")
    private String aadhaar;

    // ✅ Default Constructor
    public CustomerRequestDTO() {
    }

    // ✅ Parameterized Constructor (optional, for testing)
    public CustomerRequestDTO(String fullName, String email, String phone, String dob,
                              String address, String pan, String aadhaar) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.pan = pan;
        this.aadhaar = aadhaar;
    }

    // ✅ Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    @Override
    public String toString() {
        return "CustomerRequestDTO{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                ", address='" + address + '\'' +
                ", pan='" + pan + '\'' +
                ", aadhaar='" + aadhaar + '\'' +
                '}';
    }
}
