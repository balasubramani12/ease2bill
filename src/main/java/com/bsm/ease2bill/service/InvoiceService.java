package com.bsm.ease2bill.service;

import com.bsm.ease2bill.entity.*;
import com.bsm.ease2bill.entity.Invoice.PaymentStatus; // ✅ Import enum
import com.bsm.ease2bill.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    // ✅ Get all invoices
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // ✅ Get invoice by ID
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    // ✅ Get invoices by customer
    public List<Invoice> getInvoicesByCustomer(Long custId) {
        return invoiceRepository.findByCustomer_CustId(custId);
    }

    // ✅ Get invoices by user (cashier/admin)
    public List<Invoice> getInvoicesByUser(Long userId) {
        return invoiceRepository.findByUser_Id(userId);
    }

    // ✅ Get invoices by date
    public List<Invoice> getInvoicesByDate(LocalDate date) {
        return invoiceRepository.findByInvoiceDate(date);
    }

    // ✅ Get invoices between two dates (for monthly reports)
    public List<Invoice> getInvoicesBetweenDates(LocalDate start, LocalDate end) {
        return invoiceRepository.findByInvoiceDateBetween(start, end);
    }

    // ✅ Create new invoice with items — CORE BUSINESS LOGIC
    @Transactional
    public Invoice createInvoice(Long customerId, Long userId, List<InvoiceItem> items) {
        // 1. Validate customer and user exist
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Initialize invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setUser(user);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceTime(LocalTime.now());
        invoice.setPaymentStatus(PaymentStatus.UNPAID); // ✅ FIXED: Use enum constant

        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal totalSaved = BigDecimal.ZERO;

        // 3. Process each item
        for (InvoiceItem item : items) {
            Long prodId = item.getProduct().getProdId();
            Product product = productService.getProductById(prodId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + prodId));

            // Set product reference
            item.setProduct(product);

            // Validate quantity > 0
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            // Set price = current selling price
            item.setPrice(product.getProdSellingPrice());

            // Calculate subtotal = quantity * price
            BigDecimal subtotal = item.getQuantity().multiply(item.getPrice());
            item.setSubtotal(subtotal);

            // Calculate subsave = quantity * (MRP - sellingPrice)
            BigDecimal unitSave = product.getProdMrp().subtract(product.getProdSellingPrice());
            BigDecimal subsave = item.getQuantity().multiply(unitSave);
            item.setSubsaved(subsave);

            // Add to totals
            totalAmt = totalAmt.add(subtotal);
            totalSaved = totalSaved.add(subsave);

            // Reduce product stock
            productService.reduceStock(prodId, item.getQuantity());

            // Link item to invoice
            invoice.addInvoiceItem(item);
        }

        // 4. Set totals
        invoice.setTotalAmt(totalAmt);
        invoice.setTotalSaved(totalSaved);

        // 5. Save invoice (cascade saves all items)
        return invoiceRepository.save(invoice);
    }

    // ✅ Update payment status
    public Invoice updatePaymentStatus(Long invoiceId, PaymentStatus status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // ✅ FIXED: Validate enum value (not string)
        if (status == null || !List.of(PaymentStatus.PAID, PaymentStatus.UNPAID, PaymentStatus.PARTIAL).contains(status)) {
            throw new IllegalArgumentException("Invalid payment status");
        }

        invoice.setPaymentStatus(status);
        return invoiceRepository.save(invoice);
    }

    // ✅ Delete invoice (cascade deletes items)
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}