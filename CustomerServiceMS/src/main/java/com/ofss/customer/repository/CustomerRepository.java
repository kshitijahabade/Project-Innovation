package com.ofss.customer.repository;

import com.ofss.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByPan(String pan);
    boolean existsByAadhaar(String aadhaar);
}
