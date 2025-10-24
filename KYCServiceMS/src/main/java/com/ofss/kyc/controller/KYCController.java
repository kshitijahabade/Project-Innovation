package com.ofss.kyc.controller;

import com.ofss.kyc.entity.KYC;
import com.ofss.kyc.feign.CustomerClient;
import com.ofss.kyc.repository.KYCRepository;
import com.ofss.kyc.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/kyc")
public class KYCController {

    @Autowired
    private KYCRepository kycRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private CustomerClient customerClient;


    @Value("${customer.service.url}")
    private String customerServiceUrl;

    // ✅ Store uploaded files inside project root → /uploads
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    // =========================
    // 1️⃣ Upload Documents
    // =========================
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadKycDocuments(
            @RequestParam Long customerId,
            @RequestParam("panFile") MultipartFile panFile,
            @RequestParam("aadhaarFile") MultipartFile aadhaarFile,
            @RequestParam("photoFile") MultipartFile photoFile) {

        try {
            // ✅ Validate file types
            String[] allowedExt = {".pdf", ".jpg", ".jpeg", ".png"};
            for (MultipartFile file : List.of(panFile, aadhaarFile, photoFile)) {
                String fileName = file.getOriginalFilename().toLowerCase();
                if (Arrays.stream(allowedExt).noneMatch(fileName::endsWith)) {
                    return ResponseEntity.badRequest()
                            .body("❌ Invalid file format for " + fileName + ". Allowed: PDF, JPG, JPEG, PNG");
                }
            }

            // ✅ Save BLOBs to DB
            KYC kyc = kycRepository.findByCustomerId(customerId).orElse(new KYC());
            kyc.setCustomerId(customerId);
            kyc.setKycDate(LocalDateTime.now());

            kyc.setPanFile(panFile.getBytes());
            kyc.setAadhaarFile(aadhaarFile.getBytes());
            kyc.setPhotoFile(photoFile.getBytes());
            kyc.setStatus("PENDING");
            kyc.setRemarks(null);

            kycRepository.save(kyc);

            return ResponseEntity.ok("✅ KYC documents uploaded and saved as BLOBs successfully! Status set to PENDING.");

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("❌ File upload failed: " + e.getMessage());
        }
    }

    // =========================
    // 2️⃣ View Individual File
    // =========================
    @GetMapping("/viewfile/{customerId}/{fileType}")
    public ResponseEntity<?> viewKycFile(@PathVariable Long customerId, @PathVariable String fileType) {
        Optional<KYC> optionalKyc = kycRepository.findByCustomerId(customerId);
        if (optionalKyc.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ No KYC record found for customer " + customerId);
        }

        KYC kyc = optionalKyc.get();
        byte[] fileBytes = switch (fileType.toLowerCase()) {
            case "pan" -> kyc.getPanFile();
            case "aadhaar" -> kyc.getAadhaarFile();
            case "photo" -> kyc.getPhotoFile();
            default -> null;
        };

        if (fileBytes == null) {
            return ResponseEntity.badRequest().body("❌ Invalid fileType or file not found in DB.");
        }

        String contentType = switch (fileType.toLowerCase()) {
            case "pan", "aadhaar" -> "application/pdf";
            case "photo" -> "image/jpeg";
            default -> "application/octet-stream";
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileType + ".pdf\"")
                .body(fileBytes);
    }

    // =========================
    // 3️⃣ Get KYC by Customer ID
    // =========================
    @GetMapping("/{customerId}")
    public ResponseEntity<?> viewKycByCustomerId(@PathVariable Long customerId) {
        Optional<KYC> optionalKyc = kycRepository.findByCustomerId(customerId);
        if (optionalKyc.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ No KYC found for customer " + customerId);
        }

        KYC kyc = optionalKyc.get();
        String baseUrl = "http://localhost:8083/kyc/viewfile/" + customerId + "/";

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("customerId", customerId);
        response.put("status", kyc.getStatus());
        response.put("remarks", kyc.getRemarks());
        response.put("links", Map.of(
                "pan", baseUrl + "pan",
                "aadhaar", baseUrl + "aadhaar",
                "photo", baseUrl + "photo"
        ));

        return ResponseEntity.ok(response);
    }

    // =========================
    // 4️⃣ Approve & Reject APIs (use REST call to get email)
    // =========================

    @PutMapping("/approve/{customerId}")
    public ResponseEntity<String> approveKyc(@PathVariable Long customerId) {
        Optional<KYC> optionalKyc = kycRepository.findByCustomerId(customerId);
        if (optionalKyc.isEmpty()) return ResponseEntity.status(404).body("KYC record not found");

        KYC kyc = optionalKyc.get();
        kyc.setStatus("VERIFIED");
        kyc.setRemarks("Documents verified successfully");
        kycRepository.save(kyc);

        String email = customerClient.getCustomerEmailById(customerId);

        if (email != null) {
            String subject = "✅ KYC Verified - Customer ID: " + customerId;
            String body = "Dear Customer,\n\nYour KYC verification is complete.\nYou may now proceed with account creation.\n\nRegards,\nCustomer Onboarding Team";
            emailService.sendEmail(email, subject, body);
        }

        return ResponseEntity.ok("KYC verified and email sent successfully.");
    }

    @PutMapping("/reject/{customerId}")
    public ResponseEntity<String> rejectKyc(@PathVariable Long customerId, @RequestParam String remarks) {
        Optional<KYC> optionalKyc = kycRepository.findByCustomerId(customerId);
        if (optionalKyc.isEmpty()) return ResponseEntity.status(404).body("KYC record not found");

        KYC kyc = optionalKyc.get();
        kyc.setStatus("REJECTED");
        kyc.setRemarks(remarks);
        kycRepository.save(kyc);

        String email = customerClient.getCustomerEmailById(customerId);

        if (email != null) {
            String subject = "❌ KYC Rejected - Customer ID: " + customerId;
            String body = "Dear Customer,\n\nYour KYC verification has been rejected.\nReason: " + remarks + "\n\nPlease re-upload your documents correctly.\n\nRegards,\nCustomer Onboarding Team";
            emailService.sendEmail(email, subject, body);
        }

        return ResponseEntity.ok("KYC rejected and rejection email sent successfully.");
    }

    // =========================
    // 5️⃣ Utility - Fetch Customer Email via REST
    // =========================
    private String getCustomerEmail(Long customerId) {
        try {
            String url = customerServiceUrl + "/customers/" + customerId;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.get("email") != null) {
                return response.get("email").toString();
            }
        } catch (Exception e) {
            System.err.println("⚠️ Unable to fetch customer email from CustomerServiceMS: " + e.getMessage());
        }
        return null;
    }

    // =========================
    // 6️⃣ Get All KYCs
    // =========================
    @GetMapping("/all")
    public ResponseEntity<List<KYC>> getAllKYCs() {
        return ResponseEntity.ok(kycRepository.findAll());
    }
    
    /**
     * ✅ Filter KYCs by status (Pending / Verified / Rejected)
     * Example: GET /kyc/status/verified
     */
    @GetMapping("/kyc/status/{status}")
    public ResponseEntity<?> getKycByStatus(@PathVariable String status) {
        // Fetch all KYCs
        List<KYC> kycs = kycRepository.findAll();

        // Filter by case-insensitive status
        List<KYC> filtered = kycs.stream()
                .filter(k -> k.getStatus() != null &&
                             k.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No customers with '" + status.toUpperCase() + "' KYCs found.");
        }

        return ResponseEntity.ok(filtered);
    }

    /**
     * ✅ Summary of KYC counts
     * Example: GET /kyc/summary
     */
    @GetMapping("/kyc/summary")
    public ResponseEntity<Map<String, Long>> getKycSummary() {
        List<KYC> kycs = kycRepository.findAll();

        Map<String, Long> summary = kycs.stream()
                .filter(k -> k.getStatus() != null)
                .collect(Collectors.groupingBy(
                        k -> k.getStatus().toUpperCase(),
                        Collectors.counting()
                ));

        // Ensure all statuses are always present
        summary.putIfAbsent("VERIFIED", 0L);
        summary.putIfAbsent("PENDING", 0L);
        summary.putIfAbsent("REJECTED", 0L);

        return ResponseEntity.ok(summary);
    }
}
