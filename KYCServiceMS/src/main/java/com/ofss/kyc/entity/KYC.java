package com.ofss.kyc.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "KYC")
public class KYC {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    
    @Column(name = "kyc_date")
    private LocalDateTime kycDate;


    @Lob
    @Column(name = "pan_file", columnDefinition = "BLOB")
    private byte[] panFile;

    @Lob
    @Column(name = "aadhaar_file", columnDefinition = "BLOB")
    private byte[] aadhaarFile;

    @Lob
    @Column(name = "photo_file", columnDefinition = "BLOB")
    private byte[] photoFile;

    private String status;
    private String remarks;

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public byte[] getPanFile() { return panFile; }
    public void setPanFile(byte[] panFile) { this.panFile = panFile; }

    public byte[] getAadhaarFile() { return aadhaarFile; }
    public void setAadhaarFile(byte[] aadhaarFile) { this.aadhaarFile = aadhaarFile; }

    public byte[] getPhotoFile() { return photoFile; }
    public void setPhotoFile(byte[] photoFile) { this.photoFile = photoFile; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public LocalDateTime getKycDate() {
        return kycDate;
    }

    // Setter
    public void setKycDate(LocalDateTime kycDate) {
        this.kycDate = kycDate;
    }
}
