package com.ofss.kyc.repository;

import com.ofss.kyc.entity.KYC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KYCRepository extends JpaRepository<KYC, Long> {
    Optional<KYC> findByCustomerId(Long customerId);
    List<KYC> findByStatusIgnoreCase(String status);

}
