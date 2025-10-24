package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ofss.bank"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ofss.bank.feign")
public class BankServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankServiceMsApplication.class, args);
		System.out.println("✅ BankServiceMS instance started successfully!");
	}

}
