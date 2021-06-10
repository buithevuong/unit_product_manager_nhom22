package com.bezkoder.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_bill")
@Builder
public class SaleBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false )
    private int id;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "saleBill", cascade = CascadeType.ALL)
    private List<ItemInBill> itemInBillList;

    public void setItemInBillList(List<ItemInBill> list) {
        for(ItemInBill saleBill:list) {
            saleBill.setSaleBill(this);
        }
        this.itemInBillList = list;
    }
}
