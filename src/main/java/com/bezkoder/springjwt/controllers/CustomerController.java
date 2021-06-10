package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.exception.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.Item;
import com.bezkoder.springjwt.repository.CustomerRepository;
import com.bezkoder.springjwt.repository.SaleBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SaleBillRepository saleBillRepository;

    @GetMapping("/customers")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    @GetMapping("/customers/{cusId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Optional<Customer> getCustomerById(@PathVariable int cusId) {
        Optional<Customer> result = customerRepository.findById(cusId);
        return result;
    }

//    @GetMapping("/customers")
//    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
//    public List<Customer> getCustomerByName(String name) {
//        List<Customer> result = customerRepository.findByName(name);
//        return result;
//    }

    @PutMapping("/customers/{cusId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Customer updateCustomer(@PathVariable int cusId, @RequestBody Customer cusRequest) {
        return customerRepository.findById(cusId).map(cusNew -> {
            cusNew.setName(cusRequest.getName());
            cusNew.setDob(cusRequest.getDob());
            cusNew.setPhoneNumber(cusRequest.getPhoneNumber());
            cusNew.setMoney(cusRequest.getMoney());

            return customerRepository.save(cusNew);
        }).orElseThrow(() -> new ResourceNotFoundException("ItemId " + cusId + " not found"));
    }

    //    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/customers_statistics")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<Customer> getCustomerStatistics() {
        List<Customer> result = customerRepository.findAll(Sort.by(Sort.Direction.DESC, "money"));
        return  result;
    }

    @PostMapping("/customers")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @DeleteMapping("/customers/{customerId}")
    @Transactional
    //@PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable int customerId) {
        saleBillRepository.deleteByCustomer_Id(customerId);
        return customerRepository.findById(customerId).map(customerDelete -> {
            customerRepository.delete(customerDelete);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("CustomerId " + customerId + " not found"));
    }
}
