package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.exception.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.Supplier;
import com.bezkoder.springjwt.repository.ImportBillRepository;
import com.bezkoder.springjwt.repository.SupplierRepository;
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
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ImportBillRepository importBillRepository;


    @GetMapping("/suppliers")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<Supplier> getSupplier() {
        return supplierRepository.findAll();
    }

    @GetMapping("/suppliers/{supId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Optional<Supplier> getSupplierById(@PathVariable int supId) {
        Optional<Supplier> result = supplierRepository.findById(supId);
        return result;
    }

    @GetMapping("/suppliers_statistics")
    @PreAuthorize(" hasRole('USER')  or hasRole('ADMIN')")
    public List<Supplier> getSupplierStatistics() {
        List<Supplier> result = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "money"));
        return  result;
    }

    @PostMapping("/suppliers")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @PutMapping("/suppliers/{supplierId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Supplier updateSupplier(@PathVariable int supplierId, @RequestBody Supplier supplierRequest) {
        return supplierRepository.findById(supplierId).map(supplierNew -> {
            supplierNew.setName(supplierRequest.getName());
            supplierNew.setPhone(supplierRequest.getPhone());

            return supplierRepository.save(supplierNew);
        }).orElseThrow(() -> new ResourceNotFoundException("SupplierId " + supplierId + " not found"));
    }


    @DeleteMapping("/suppliers/{supplierId}")
    @Transactional
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> deleteSupplier(@PathVariable int supplierId) {
        importBillRepository.deleteBySupplier_Id(supplierId);
        return supplierRepository.findById(supplierId).map(supplierDelete -> {
            supplierRepository.delete(supplierDelete);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("SupplierId " + supplierId + " not found"));
    }
}
