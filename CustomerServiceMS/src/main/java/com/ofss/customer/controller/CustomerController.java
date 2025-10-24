package com.ofss.customer.controller;

import com.ofss.customer.dto.CustomerRequestDTO;
import com.ofss.customer.dto.CustomerResponseDTO;
import com.ofss.customer.dto.CustomerViewDTO;
import com.ofss.customer.entity.Customer;
import com.ofss.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> registerCustomer(
            @Valid @RequestBody CustomerRequestDTO request) {

        // duplicate check (optional for real apps)
        if (customerRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new CustomerResponseDTO(null, "Email already registered"));
        }

        // create entity from request
        Customer customer = new Customer(
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                request.getDob(),
                request.getAddress(),
                request.getPan(),
                request.getAadhaar()
        );

        // save to DB
        customerRepository.save(customer);

        // build response
        String customerId = "CUST-" + customer.getId();
        CustomerResponseDTO response =
                new CustomerResponseDTO(customerId, "Customer registered successfully");

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO request) {

        return customerRepository.findById(id).map(existing -> {
            // update each field
            existing.setFullName(request.getFullName());
            existing.setEmail(request.getEmail());
            existing.setPhone(request.getPhone());
            existing.setDob(request.getDob());
            existing.setAddress(request.getAddress());
            existing.setPan(request.getPan());
            existing.setAadhaar(request.getAadhaar());

            customerRepository.save(existing);

            CustomerResponseDTO response = new CustomerResponseDTO(
                    "CUST-" + existing.getId(),
                    "Customer details updated successfully"
            );
            return ResponseEntity.ok(response);

        }).orElseGet(() ->
                ResponseEntity.status(404)
                        .body(new CustomerResponseDTO(null, "Customer not found")));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> deleteCustomer(@PathVariable Long id) {
        return customerRepository.findById(id).map(existing -> {
            customerRepository.delete(existing);

            CustomerResponseDTO response = new CustomerResponseDTO(
                    "CUST-" + id,
                    "Customer deleted successfully"
            );
            return ResponseEntity.ok(response);

        }).orElseGet(() ->
                ResponseEntity.status(404)
                        .body(new CustomerResponseDTO(null, "Customer not found")));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> patchCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequestDTO request) {

        return customerRepository.findById(id).map(existing -> {

            if (request.getFullName() != null)
                existing.setFullName(request.getFullName());

            if (request.getEmail() != null)
                existing.setEmail(request.getEmail());

            if (request.getPhone() != null)
                existing.setPhone(request.getPhone());

            if (request.getDob() != null)
                existing.setDob(request.getDob());

            if (request.getAddress() != null)
                existing.setAddress(request.getAddress());

            if (request.getPan() != null)
                existing.setPan(request.getPan());

            if (request.getAadhaar() != null)
                existing.setAadhaar(request.getAadhaar());

            customerRepository.save(existing);

            CustomerResponseDTO response = new CustomerResponseDTO(
                    "CUST-" + existing.getId(),
                    "Customer details updated partially"
            );

            return ResponseEntity.ok(response);

        }).orElseGet(() ->
                ResponseEntity.status(404)
                        .body(new CustomerResponseDTO(null, "Customer not found")));
    }



    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerViewDTO> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(c -> new CustomerViewDTO(
                        c.getId(),
                        c.getFullName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getDob(),
                        c.getAddress(),
                        c.getPan(),
                        c.getAadhaar()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    


    
    @GetMapping("/all")
    public ResponseEntity<List<CustomerViewDTO>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerViewDTO> result = customers.stream()
                .map(c -> new CustomerViewDTO(
                        c.getId(),
                        c.getFullName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getDob(),
                        c.getAddress(),
                        c.getPan(),
                        c.getAadhaar()))
                .toList();

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/email/{customerId}")
    public ResponseEntity<String> getCustomerEmailById(@PathVariable Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> ResponseEntity.ok(customer.getEmail()))
                .orElse(ResponseEntity.notFound().build());
    }


}
