package com.ofss.dashboard.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "CustomerServiceMS")
public interface CustomerClient {

    @GetMapping("/customers/all")
    List<Map<String, Object>> getAllCustomers();

    @GetMapping("/customers/{id}")
    Map<String, Object> getCustomerById(@PathVariable("id") Long id);
}
