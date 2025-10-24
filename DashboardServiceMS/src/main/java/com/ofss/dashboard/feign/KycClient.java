package com.ofss.dashboard.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "KYCServiceMS")
public interface KycClient {

    @GetMapping("/kyc/all")
    List<Map<String, Object>> getAllKycs();
}
