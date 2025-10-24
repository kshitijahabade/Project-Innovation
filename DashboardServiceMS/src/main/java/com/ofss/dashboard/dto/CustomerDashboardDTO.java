package com.ofss.dashboard.dto;

import java.util.List;
import java.util.Map;

public class CustomerDashboardDTO {
    private Map<String, Object> customer;
    private List<Map<String, Object>> accounts;
    private Map<String, Object> kyc;

    public CustomerDashboardDTO() {}

    public CustomerDashboardDTO(Map<String, Object> customer, List<Map<String, Object>> accounts, Map<String, Object> kyc) {
        this.customer = customer;
        this.accounts = accounts;
        this.kyc = kyc;
    }

    public Map<String, Object> getCustomer() { return customer; }
    public void setCustomer(Map<String, Object> customer) { this.customer = customer; }

    public List<Map<String, Object>> getAccounts() { return accounts; }
    public void setAccounts(List<Map<String, Object>> accounts) { this.accounts = accounts; }

    public Map<String, Object> getKyc() { return kyc; }
    public void setKyc(Map<String, Object> kyc) { this.kyc = kyc; }
}
