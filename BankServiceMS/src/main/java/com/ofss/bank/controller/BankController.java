package com.ofss.bank.controller;

import com.ofss.bank.dto.BankRequestDTO;
import com.ofss.bank.dto.BankResponseDTO;
import com.ofss.bank.dto.BankViewDTO;
import com.ofss.bank.entity.Bank;
import com.ofss.bank.repository.BankRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/banks")
public class BankController {

    @Autowired
    private BankRepository bankRepository;

    // ✅ Register a new Bank
    @PostMapping("/register")
    public ResponseEntity<BankResponseDTO> registerBank(@Valid @RequestBody BankRequestDTO request) {
        if (bankRepository.existsByIfscCode(request.getIfscCode())) {
            return ResponseEntity.badRequest()
                    .body(new BankResponseDTO(null, "Bank with this IFSC already exists"));
        }

        Bank bank = new Bank(request.getName(), request.getIfscCode(), request.getAddress());
        bankRepository.save(bank);

        String bankId = "BANK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return ResponseEntity.ok(new BankResponseDTO(bankId, "Bank registered successfully"));
    }

    // ✅ Get all Banks
    @GetMapping("/all")
    public ResponseEntity<List<Bank>> getAllBanks() {
        return ResponseEntity.ok(bankRepository.findAll());
    }

    // ✅ Get Bank by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBankById(@PathVariable Long id) {
        return bankRepository.findById(id)
                .<ResponseEntity<?>>map(bank -> {
                    BankViewDTO view = new BankViewDTO(
                            bank.getId(),
                            bank.getName(),
                            bank.getIfscCode(),
                            bank.getAddress()
                    );
                    return ResponseEntity.ok(view);
                })
                .orElse(ResponseEntity.status(404)
                        .body(new BankResponseDTO(null, "Bank not found with ID: " + id)));
    }

    // ✅ Update entire Bank (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<BankResponseDTO> updateBank(
            @PathVariable Long id,
            @Valid @RequestBody BankRequestDTO request) {

        Bank bank = bankRepository.findById(id).orElse(null);
        if (bank == null) {
            return ResponseEntity.status(404)
                    .body(new BankResponseDTO(null, "Bank not found with ID: " + id));
        }

        bank.setName(request.getName());
        bank.setIfscCode(request.getIfscCode());
        bank.setAddress(request.getAddress());
        bankRepository.save(bank);

        return ResponseEntity.ok(
                new BankResponseDTO("BANK-" + id, "Bank details updated successfully"));
    }

    // ✅ Partial Update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<BankResponseDTO> partiallyUpdateBank(
            @PathVariable Long id,
            @RequestBody BankRequestDTO request) {

        Bank bank = bankRepository.findById(id).orElse(null);
        if (bank == null) {
            return ResponseEntity.status(404)
                    .body(new BankResponseDTO(null, "Bank not found with ID: " + id));
        }

        if (request.getName() != null) bank.setName(request.getName());
        if (request.getIfscCode() != null) bank.setIfscCode(request.getIfscCode());
        if (request.getAddress() != null) bank.setAddress(request.getAddress());
        bankRepository.save(bank);

        return ResponseEntity.ok(
                new BankResponseDTO("BANK-" + id, "Bank details partially updated"));
    }

    // ✅ Delete Bank by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<BankResponseDTO> deleteBank(@PathVariable Long id) {
        if (!bankRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(new BankResponseDTO(null, "Bank not found with ID: " + id));
        }

        bankRepository.deleteById(id);
        return ResponseEntity.ok(
                new BankResponseDTO("BANK-" + id, "Bank deleted successfully"));
    }
}
