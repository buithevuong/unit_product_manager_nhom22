package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.ItemInBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInBillRepository extends JpaRepository<ItemInBill, Integer> {
//    ItemInBill deleteBy
}
