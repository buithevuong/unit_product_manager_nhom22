package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.SaleBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleBillRepository extends JpaRepository<SaleBill, Integer> {
    List<SaleBill> findByCustomer(Customer customer);

    @Modifying
    void deleteByCustomer_Id(int id);
}