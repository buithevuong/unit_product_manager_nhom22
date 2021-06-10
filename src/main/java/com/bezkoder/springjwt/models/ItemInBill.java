package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_in_bill")
@Builder
public class ItemInBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false )
    private int id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "output_price")
    private int outputPrice;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item_id", nullable = false, referencedColumnName = "id")
    private Item item;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sale_bill_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference("itemsale_salebill")
    private SaleBill saleBill;
}
