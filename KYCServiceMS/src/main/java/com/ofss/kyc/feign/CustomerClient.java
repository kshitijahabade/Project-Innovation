package com.ofss.kyc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CustomerServiceMS")  // matches the spring.application.name of CustomerServiceMS
public interface CustomerClient {

    @GetMapping("/customers/email/{customerId}")
    String getCustomerEmailById(@PathVariable("customerId") Long customerId);
}
