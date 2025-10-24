# Project-Innovation
🏦 Microservices-Based Banking Application
A Spring Boot microservices system with Eureka Discovery, Feign Clients, and Oracle Database — handling modular operations for Customer, KYC, Account, Bank, and Dashboard services.
🧩 Microservices Overview
Service	Port	Description
EurekaServerMS	8761	Service registry for all microservices
CustomerServiceMS	8081	Handles customer registration and validation
BankServiceMS	8085	Manages bank and IFSC details
KYCServiceMS	8083	Processes KYC uploads, approvals, and re-uploads
AccountServiceMS	8084	Handles account creation and transactions
DashboardServiceMS	8086	Aggregates data from all services for analytics
⚙️ Tech Stack
• Java 17, Spring Boot 3.x
• Spring Cloud Netflix Eureka, OpenFeign
• Oracle SQL Developer
• Maven 3.9+
🏗️ Build & Run Instructions
1️⃣ Prerequisites: JDK 17+, Maven, Oracle DB configured in each application.properties
2️⃣ Build each service:
   mvn clean install
3️⃣ Run in order:
   1. EurekaServerMS
   2. CustomerServiceMS
   3. BankServiceMS
   4. KYCServiceMS
   5. AccountServiceMS
   6. DashboardServiceMS
🌐 Access URLs
Eureka Dashboard → http://localhost:8761
Swagger UI (each service) → http://localhost:<port>/swagger-ui/index.html
🧪 Test Flow
1. Register customer → /customers/register
2. Add bank → /banks/add
3. Upload and approve KYC → /kyc/upload
4. Create account → /accounts/create (only if KYC verified)
5. View dashboard → /dashboard/{customerId}
🚀 Highlights
• Feign-based inter-service communication
• Auto-generated account numbers
• Centralized discovery via Eureka
• Oracle-based persistent storage

