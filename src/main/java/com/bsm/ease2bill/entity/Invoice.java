package com.bsm.ease2bill.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // ✅ FIXED: Import BigDecimal
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @ManyToOne
    @JoinColumn(name = "cust_id", nullable = false) // ✅ FIXED: removed cascade (dangerous), added nullable=false
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // ✅ FIXED: removed cascade, added nullable=false
    private User user;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "invoice_time")
    private LocalTime invoiceTime;

    @Column(name = "total_amt")
    private BigDecimal totalAmt; // ✅ FIXED: double → BigDecimal

    @Column(name = "total_save") // ✅ FIXED: matches DB column "total_save"
    private BigDecimal totalSaved; // ✅ FIXED: double → BigDecimal

    @Column(name = "payment_status", columnDefinition = "ENUM('PAID', 'UNPAID', 'PARTIAL')")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true) // ✅ FIXED: Simplified + orphanRemoval
    private List<InvoiceItem> invoiceItems = new ArrayList<>(); // ✅ FIXED: Initialize list

    public Invoice() {}

    public enum PaymentStatus {
    PAID, UNPAID, PARTIAL
    }

    // ✅ FIXED: Constructor uses BigDecimal
    public Invoice(LocalDate invoiceDate, LocalTime invoiceTime,
                   BigDecimal totalAmt, BigDecimal totalSaved, PaymentStatus paymentStatus) {
        this.invoiceDate = invoiceDate;
        this.invoiceTime = invoiceTime;
        this.totalAmt = totalAmt;
        this.totalSaved = totalSaved;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalTime getInvoiceTime() { return invoiceTime; }
    public void setInvoiceTime(LocalTime invoiceTime) { this.invoiceTime = invoiceTime; }

    public BigDecimal getTotalAmt() { return totalAmt; } // ✅ FIXED: double → BigDecimal
    public void setTotalAmt(BigDecimal totalAmt) { this.totalAmt = totalAmt; }

    public BigDecimal getTotalSaved() { return totalSaved; } // ✅ FIXED: double → BigDecimal
    public void setTotalSaved(BigDecimal totalSaved) { this.totalSaved = totalSaved; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public List<InvoiceItem> getInvoiceItems() { return invoiceItems; }
    public void setInvoiceItems(List<InvoiceItem> invoiceItems) { this.invoiceItems = invoiceItems; }

    public void addInvoiceItem(InvoiceItem item) {
        invoiceItems.add(item);
        item.setInvoice(this);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", invoiceDate=" + invoiceDate +
                ", invoiceTime=" + invoiceTime +
                ", totalAmt=" + totalAmt +
                ", totalSaved=" + totalSaved +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}