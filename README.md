🏦 Project-Innovation: Microservices-Based Banking Application

A Spring Boot microservices system with Eureka Discovery, OpenFeign, and Oracle Database — handling modular operations for Customer, KYC, Account, Bank, and Dashboard services.

🧩 Microservices Overview
Service	Port	Description
EurekaServerMS	8761	Service registry for all microservices
CustomerServiceMS	8081	Handles customer registration and validation
BankServiceMS	8085	Manages bank and IFSC details
KYCServiceMS	8083	Processes KYC uploads, approvals, and re-uploads
AccountServiceMS	8084	Handles account creation and transactions
DashboardServiceMS	8086	Aggregates data from all services for analytics
⚙️ Tech Stack

Java 17

Spring Boot 3.x

Spring Cloud Netflix Eureka

Spring Cloud OpenFeign

Oracle SQL Developer

Maven 3.9+

🏗️ Build & Run Instructions
1️⃣ Prerequisites

Install JDK 17+ and Maven

Ensure Oracle Database is running

Update database credentials in each microservice’s application.properties

2️⃣ Build Each Service
mvn clean install

3️⃣ Run in Order

EurekaServerMS

CustomerServiceMS

BankServiceMS

KYCServiceMS

AccountServiceMS

DashboardServiceMS

🌐 Access URLs
Component	URL
Eureka Dashboard	http://localhost:8761

Swagger UI (per service)	http://localhost:`
<port>`/swagger-ui/index.html

🚀 Highlights

✅ Feign-based inter-service communication

✅ Auto-generated account numbers

✅ Centralized discovery using Eureka

✅ Oracle-backed persistent storage

✅ Modular REST architecture with fault isolation
