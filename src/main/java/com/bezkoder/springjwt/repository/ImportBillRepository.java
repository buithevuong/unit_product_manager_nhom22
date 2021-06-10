package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.ImportBill;
import com.bezkoder.springjwt.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportBillRepository extends JpaRepository<ImportBill, Integer> {
    List<ImportBill> findBySupplier(Supplier supplierId);

    @Modifying
    void deleteBySupplier_Id(int id);
}
