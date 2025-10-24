package com.ofss.dashboard.controller;

import com.ofss.dashboard.dto.CustomerDashboardDTO;
import com.ofss.dashboard.feign.CustomerClient;
import com.ofss.dashboard.feign.AccountClient;
import com.ofss.dashboard.feign.KycClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired private CustomerClient customerClient;
    @Autowired private AccountClient accountClient;
    @Autowired private KycClient kycClient;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerDashboard(@PathVariable Long customerId) {
        Map<String, Object> customer = customerClient.getCustomerById(customerId);
        if (customer == null || customer.isEmpty()) {
            return ResponseEntity.status(404).body("Customer not found");
        }

        // Filter accounts by customerId
        List<Map<String, Object>> accounts = accountClient.getAllAccounts().stream()
                .filter(a -> Objects.equals(
                        Long.valueOf(a.get("customerId").toString()), 
                        customerId))
                .collect(Collectors.toList());

        // Filter KYC by customerId
        Map<String, Object> kyc = kycClient.getAllKycs().stream()
                .filter(k -> Objects.equals(
                        Long.valueOf(k.get("customerId").toString()), 
                        customerId))
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(new CustomerDashboardDTO(customer, accounts, kyc));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDashboards() {
        List<Map<String, Object>> customers = customerClient.getAllCustomers();
        List<Map<String, Object>> accounts = accountClient.getAllAccounts();
        List<Map<String, Object>> kycs = kycClient.getAllKycs();

        List<CustomerDashboardDTO> dashboards = customers.stream().map(c -> {
            Long cid = ((Number) c.get("id")).longValue();

            // Match accounts by customerId
            List<Map<String, Object>> accs = accounts.stream()
                    .filter(a -> a.get("customerId") != null &&
                                 Objects.equals(Long.valueOf(a.get("customerId").toString()), cid))
                    .collect(Collectors.toList());

            // Match KYC by customerId
            Map<String, Object> k = kycs.stream()
                    .filter(kx -> kx.get("customerId") != null &&
                                  Objects.equals(Long.valueOf(kx.get("customerId").toString()), cid))
                    .findFirst()
                    .orElse(null);

            return new CustomerDashboardDTO(c, accs, k);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dashboards);
    }


    
}
