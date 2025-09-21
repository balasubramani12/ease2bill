package com.bsm.ease2bill.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // ✅ FIXED: Import BigDecimal
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long prodId;

    @Column(name = "name", nullable = false)
    private String prodName;

    @Column(name = "category")
    private String prodCat;

    @Column(name = "mrp")
    private BigDecimal prodMrp; // ✅ FIXED: double → BigDecimal

    @Column(name = "selling_price")
    private BigDecimal prodSellingPrice; // ✅ FIXED: double → BigDecimal

    @Column(name = "description") // ✅ FIXED: "desc" → "description" (SQL reserved word)
    private String prodDesc;

    @Column(name = "upc", unique = true)
    private String prodSku;  // SKU/Barcode

    @Column(name = "units")
    private String prodUnits;

    @Column(name = "stock")
    private BigDecimal stock; // ✅ FIXED: double → BigDecimal

    @Column(name = "opening_stock")
    private BigDecimal openingStock;  // ✅ FIXED: double → BigDecimal

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) // ✅ FIXED: Simplified cascade + orphanRemoval
    private List<InvoiceItem> invoiceItems = new ArrayList<>(); // ✅ FIXED: Initialize list

    public Product() {}

    // ✅ FIXED: Constructor uses BigDecimal
    public Product(String prodName, String prodCat, BigDecimal prodMrp, BigDecimal prodSellingPrice,
                   String prodDesc, String prodSku, String prodUnits, BigDecimal stock, BigDecimal openingStock) {
        this.prodName = prodName;
        this.prodCat = prodCat;
        this.prodMrp = prodMrp;
        this.prodSellingPrice = prodSellingPrice;
        this.prodDesc = prodDesc;
        this.prodSku = prodSku;
        this.prodUnits = prodUnits;
        this.stock = stock;
        this.openingStock = openingStock;
    }

    // Getters and Setters (all updated to BigDecimal)
    public Long getProdId() { return prodId; }
    public void setProdId(Long prodId) { this.prodId = prodId; }

    public String getProdName() { return prodName; }
    public void setProdName(String prodName) { this.prodName = prodName; }

    public String getProdCat() { return prodCat; }
    public void setProdCat(String prodCat) { this.prodCat = prodCat; }

    public BigDecimal getProdMrp() { return prodMrp; } // ✅ FIXED: double → BigDecimal
    public void setProdMrp(BigDecimal prodMrp) { this.prodMrp = prodMrp; }

    public BigDecimal getProdSellingPrice() { return prodSellingPrice; } // ✅ FIXED: double → BigDecimal
    public void setProdSellingPrice(BigDecimal prodSellingPrice) { this.prodSellingPrice = prodSellingPrice; }

    public String getProdDesc() { return prodDesc; }
    public void setProdDesc(String prodDesc) { this.prodDesc = prodDesc; }

    public String getProdSku() { return prodSku; }
    public void setProdSku(String prodSku) { this.prodSku = prodSku; }

    public String getProdUnits() { return prodUnits; }
    public void setProdUnits(String prodUnits) { this.prodUnits = prodUnits; }

    public BigDecimal getStock() { return stock; } // ✅ FIXED: double → BigDecimal
    public void setStock(BigDecimal stock) { this.stock = stock; }

    public BigDecimal getOpeningStock() { return openingStock; } // ✅ FIXED: double → BigDecimal
    public void setOpeningStock(BigDecimal openingStock) { this.openingStock = openingStock; }

    public List<InvoiceItem> getInvoiceItems() { return invoiceItems; }
    public void setInvoiceItems(List<InvoiceItem> invoiceItems) { this.invoiceItems = invoiceItems; }

    public void addInvoiceItem(InvoiceItem item) {
        invoiceItems.add(item);
        item.setProduct(this);
    }

    @Override
    public String toString() {
        return "Product{" +
                "prodId=" + prodId +
                ", prodName='" + prodName + '\'' +
                ", prodCat='" + prodCat + '\'' +
                ", prodMrp=" + prodMrp +
                ", prodSellingPrice=" + prodSellingPrice +
                ", prodDesc='" + prodDesc + '\'' +
                ", prodSku='" + prodSku + '\'' +
                ", prodUnits='" + prodUnits + '\'' +
                ", stock=" + stock +
                ", openingStock=" + openingStock +
                '}';
    }
}