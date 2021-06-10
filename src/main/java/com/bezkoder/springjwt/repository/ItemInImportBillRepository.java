package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Item;
import com.bezkoder.springjwt.models.ItemInImportBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemInImportBillRepository extends JpaRepository<ItemInImportBill, Integer> {
    List<ItemInImportBill> findByItem(Item item);
}
