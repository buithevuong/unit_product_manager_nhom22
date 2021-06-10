package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.exception.ResourceNotFoundException;
import com.bezkoder.springjwt.models.ImportBill;
import com.bezkoder.springjwt.models.ItemInImportBill;
import com.bezkoder.springjwt.models.Supplier;
import com.bezkoder.springjwt.repository.ImportBillRepository;
import com.bezkoder.springjwt.repository.ItemRepository;
import com.bezkoder.springjwt.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ImportBillController {
    @Autowired
    private ImportBillRepository importBillRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping("/import_bill")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<ImportBill> getAllImportBill() {

        return importBillRepository.findAll();
    }

    @GetMapping("/import_bill/{supplierId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<ImportBill> getAllBySupplier(@PathVariable int supplierId ) {
        Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        return importBillRepository.findBySupplier(supplier);
    }

    @PostMapping("/import_bill")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ImportBill createImportBill(@RequestBody ImportBill importBill) {
        int total = 0;
        ImportBill result = importBillRepository.save(importBill);
        List<ItemInImportBill> itemInImportBillList = importBill.getItemInImportBillList();
        for (int i = 0; i < itemInImportBillList.size(); i++) {
            ItemInImportBill itemImport = itemInImportBillList.get(i);
            total = total + (itemImport.getQuantity() * itemImport.getInputPrice());
            itemRepository.findById(itemImport.getItem().getId()).map(itemNew -> {
                itemNew.setStatus(itemNew.getStatus() + itemImport.getQuantity());
                itemRepository.save(itemNew);
                return 1;
            });
        }
        int finalTotal = total;
        supplierRepository.findById(importBill.getSupplier().getId()).map(supplier -> {
            supplier.setMoney(supplier.getMoney() + finalTotal);
            supplierRepository.save(supplier);
            return 1;
        });
        return result;
    }

    @DeleteMapping("/import_bill/{importBillId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> deleteImportBill(@PathVariable int importBillId) {
        return importBillRepository.findById(importBillId).map(importBillDelete -> {
            importBillRepository.delete(importBillDelete);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ImportBillId " + importBillId + " not found"));
    }
}

