package com.bsm.ease2bill.service;

import com.bsm.ease2bill.entity.InvoiceItem;
import com.bsm.ease2bill.repository.InvoiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    // ✅ Get all invoice items (rarely used directly)
    public List<InvoiceItem> getAllInvoiceItems() {
        return invoiceItemRepository.findAll();
    }

    // ✅ Get item by ID
    public Optional<InvoiceItem> getInvoiceItemById(Long id) {
        return invoiceItemRepository.findById(id);
    }

    // ✅ Get all items for a given invoice
    public List<InvoiceItem> getItemsByInvoiceId(Long invoiceId) {
        return invoiceItemRepository.findByInvoice_InvoiceId(invoiceId);
    }

    // ✅ Get all items for a given product (across all invoices)
    public List<InvoiceItem> getItemsByProductId(Long prodId) {
        return invoiceItemRepository.findByProduct_ProdId(prodId);
    }

    // ✅ Save or update item (usually called via InvoiceService cascade)
    public InvoiceItem saveInvoiceItem(InvoiceItem item) {
        return invoiceItemRepository.save(item);
    }

    // ✅ Delete item by ID (usually handled via orphanRemoval in Invoice)
    public void deleteInvoiceItem(Long id) {
        invoiceItemRepository.deleteById(id);
    }
}