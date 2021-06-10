package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.exception.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.Item;
import com.bezkoder.springjwt.models.ItemInBill;
import com.bezkoder.springjwt.models.SaleBill;
import com.bezkoder.springjwt.repository.CustomerRepository;
import com.bezkoder.springjwt.repository.ItemRepository;
import com.bezkoder.springjwt.repository.SaleBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class SaleBillController {
    @Autowired
    private SaleBillRepository saleBillRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/sale_bill")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<SaleBill> getSaleBill() {
        return saleBillRepository.findAll();
    }

    @GetMapping("/sale_bill/{customerId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<SaleBill> getAllByCustomerId(@PathVariable int customerId ) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return saleBillRepository.findByCustomer(customer);
    }


    @PostMapping("/sale_bill")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public SaleBill createSaleBill(@RequestBody SaleBill saleBill) {
        int total = 0;
        SaleBill result = saleBillRepository.save(saleBill);
        List<ItemInBill> itemInBillList = saleBill.getItemInBillList();
        for (int i = 0; i < itemInBillList.size(); i++) {
            ItemInBill itemInBill = itemInBillList.get(i);
//            Optional<Item> item = itemRepository.findById(itemInBill.getItem().getId());
            total = total + (itemInBill.getQuantity() * itemInBill.getOutputPrice());
            itemRepository.findById(itemInBill.getItem().getId()).map(itemNew -> {
                itemNew.setStatus(itemNew.getStatus() - itemInBill.getQuantity());
                itemRepository.save(itemNew);
                return 1;
            });
        }
        int finalTotal = total;
        customerRepository.findById(saleBill.getCustomer().getId()).map(customer -> {
            customer.setMoney(customer.getMoney() + finalTotal);
            customerRepository.save(customer);
            return 1;
        });
        return result;
    }

    @DeleteMapping("/sale_bill/{billId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> deleteSaleBill(@PathVariable int billId) {
        return saleBillRepository.findById(billId).map(saleBillDelete -> {
            saleBillRepository.delete(saleBillDelete);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ItemId " + billId + " not found"));
    }
}
