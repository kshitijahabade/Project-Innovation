package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.ofss.customer"})
@EnableDiscoveryClient
public class CustomerServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceMsApplication.class, args);
		System.out.println("CustomerServiceMS instance started successfully!");
	}

}
