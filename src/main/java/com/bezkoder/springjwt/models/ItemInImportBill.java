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
@Table(name = "item_in_import_bill")
@Builder
public class ItemInImportBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false )
    private int id;

    @Column(name = "input_price")
    private int inputPrice;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "item_id", nullable = false, referencedColumnName = "id")
    private Item item;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "import_bill_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference("itemimport_importbill")
    private ImportBill importBill;
}