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
@Table(name = "import_bill")
@Builder
public class ImportBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false )
    private int id;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false, referencedColumnName = "id")
    private Supplier  supplier;

    @OneToMany(mappedBy = "importBill", cascade = CascadeType.ALL)
    private List<ItemInImportBill> itemInImportBillList;

    public void setItemInImportBillList(List<ItemInImportBill> list) {
        for(ItemInImportBill importBill:list) {
            importBill.setImportBill(this);
        }
        this.itemInImportBillList = list;
    }
}
