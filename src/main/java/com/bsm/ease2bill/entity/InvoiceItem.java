package com.bsm.ease2bill.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // ✅ FIXED: Import BigDecimal

@Entity
@Table(name = "invoice_item")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_item_id")
    private Long invoiceItemId;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false) // ✅ FIXED: Removed cascade, added nullable=false
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "prod_id", nullable = false) // ✅ FIXED: Removed cascade, added nullable=false
    private Product product;

    @Column(name = "quantity")
    private BigDecimal quantity; // ✅ FIXED: double → BigDecimal

    @Column(name = "price")
    private BigDecimal price; // ✅ FIXED: double → BigDecimal

    @Column(name = "subtotal")
    private BigDecimal subtotal; // ✅ FIXED: double → BigDecimal

    @Column(name = "subsave")
    private BigDecimal subsaved; // ✅ FIXED: double → BigDecimal

    public InvoiceItem() {}

    // ✅ FIXED: Constructor uses BigDecimal
    public InvoiceItem(BigDecimal quantity, BigDecimal price, BigDecimal subtotal, BigDecimal subsaved) {
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
        this.subsaved = subsaved;
    }

    // Getters and Setters
    public Long getInvoiceItemId() { // ✅ FIXED: was getInvoiceItem() → misleading
        return invoiceItemId;
    }

    public void setInvoiceItemId(Long invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public BigDecimal getQuantity() { return quantity; } // ✅ FIXED: double → BigDecimal
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; } // ✅ FIXED: double → BigDecimal
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getSubtotal() { return subtotal; } // ✅ FIXED: double → BigDecimal
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getSubsaved() { return subsaved; } // ✅ FIXED: double → BigDecimal
    public void setSubsaved(BigDecimal subsaved) { this.subsaved = subsaved; }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "invoiceItemId=" + invoiceItemId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                ", subsaved=" + subsaved +
                '}';
    }
}