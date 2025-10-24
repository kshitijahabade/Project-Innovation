package com.ofss.customer.dto;

public class CustomerResponseDTO {

    private String customerId;
    private String message;

    // ✅ Default Constructor
    public CustomerResponseDTO() {
    }

    // ✅ Parameterized Constructor
    public CustomerResponseDTO(String customerId, String message) {
        this.customerId = customerId;
        this.message = message;
    }

    // ✅ Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomerResponseDTO{" +
                "customerId='" + customerId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
