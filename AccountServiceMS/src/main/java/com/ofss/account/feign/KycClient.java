// AccountServiceMS/src/main/java/com/ofss/account/feign/KycClient.java
package com.ofss.account.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "KYCServiceMS")
public interface KycClient {
  @GetMapping("/kyc/{customerId}")
  Map<String, Object> getKyc(@PathVariable("customerId") Long customerId);
}
