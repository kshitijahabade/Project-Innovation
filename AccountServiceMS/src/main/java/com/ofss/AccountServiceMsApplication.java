package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ofss.account"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ofss.account.feign")
public class AccountServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceMsApplication.class, args);
		System.out.println("✅ AccountServiceMS instance started successfully!");
	}

}
