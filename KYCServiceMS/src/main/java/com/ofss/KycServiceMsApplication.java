package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ofss.kyc"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ofss.kyc.feign")
public class KycServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KycServiceMsApplication.class, args);
		System.out.println("KYCServiceMS instance started successfully!");
	}

}
