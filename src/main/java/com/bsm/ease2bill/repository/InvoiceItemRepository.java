package com.bsm.ease2bill.repository;

import com.bsm.ease2bill.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    // ✅ Find all items for a given invoice
    List<InvoiceItem> findByInvoice_InvoiceId(Long invoiceId);

    // ✅ Find all items for a given product (across all invoices)
    List<InvoiceItem> findByProduct_ProdId(Long prodId);

    // ✅ Find by invoice + product (rare, but useful for audits)
    List<InvoiceItem> findByInvoice_InvoiceIdAndProduct_ProdId(Long invoiceId, Long prodId);
}