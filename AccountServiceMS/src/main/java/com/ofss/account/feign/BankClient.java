// AccountServiceMS/src/main/java/com/ofss/account/feign/BankClient.java
package com.ofss.account.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BankServiceMS")
public interface BankClient {
	@GetMapping("/banks/{id}")
	  Object getBankById(@PathVariable("id") Long id);
}
