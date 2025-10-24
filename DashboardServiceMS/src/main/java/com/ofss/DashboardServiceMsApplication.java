package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ofss.dashboard"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ofss.dashboard.feign")
public class DashboardServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardServiceMsApplication.class, args);
		System.out.println("✅ DashboardServiceMS instance started successfully!");
	}

}
