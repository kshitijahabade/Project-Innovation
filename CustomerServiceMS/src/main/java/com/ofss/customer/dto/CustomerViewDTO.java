package com.ofss.customer.dto;

public class CustomerViewDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String dob;
    private String address;
    private String pan;
    private String aadhaar;

    public CustomerViewDTO() {}

    public CustomerViewDTO(Long id, String fullName, String email, String phone,
                           String dob, String address, String pan, String aadhaar) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.pan = pan;
        this.aadhaar = aadhaar;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }
}
