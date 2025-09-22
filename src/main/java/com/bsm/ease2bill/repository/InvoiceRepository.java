package com.bsm.ease2bill.repository;

import com.bsm.ease2bill.entity.Invoice;
import com.bsm.ease2bill.entity.Invoice.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // ✅ Find all invoices for a customer
    List<Invoice> findByCustomer_CustId(Long custId);

    // ✅ Find all invoices created by a user
    List<Invoice> findByUser_Id(Long userId);

    // ✅ Find invoices by date
    List<Invoice> findByInvoiceDate(LocalDate date);

    // ✅ Find invoices between two dates (for monthly reports)
    List<Invoice> findByInvoiceDateBetween(LocalDate start, LocalDate end);

    // ✅ Find by payment status
    List<Invoice> findByPaymentStatus(PaymentStatus status); // ✅ This is fine — Spring Data handles enums
}