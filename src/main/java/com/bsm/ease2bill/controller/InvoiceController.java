package com.bsm.ease2bill.controller;

import com.bsm.ease2bill.entity.*;
import com.bsm.ease2bill.service.*;
import com.bsm.ease2bill.util.QRCodeGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // ✅ Show invoice creation form
    @GetMapping("/new")
    public String showNewInvoiceForm(Model model) {
        // Get all customers for dropdown
        model.addAttribute("customers", customerService.getAllCustomers());
        // Get all products for search/selection
        model.addAttribute("products", productService.getAllProducts());
        // Initialize empty cart in session (or use hidden form)
        model.addAttribute("invoiceItems", new ArrayList<InvoiceItem>());
        model.addAttribute("selectedCustomer", new Customer());
        return "invoice/new"; // templates/invoice/new.html
    }

    // ✅ Handle adding product to cart (AJAX or form post — we'll do form for simplicity)
    @PostMapping("/add-item")
    public String addItemToCart(@RequestParam Long productId,
                                @RequestParam BigDecimal quantity,
                                @ModelAttribute("invoiceItems") List<InvoiceItem> items,
                                Model model) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        InvoiceItem item = new InvoiceItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getProdSellingPrice());
        item.setSubtotal(quantity.multiply(product.getProdSellingPrice()));
        item.setSubsaved(quantity.multiply(product.getProdMrp().subtract(product.getProdSellingPrice())));

        items.add(item);

        model.addAttribute("invoiceItems", items);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("selectedCustomer", new Customer());

        return "invoice/new";
    }

    // ✅ Handle saving invoice
    @PostMapping("/save")
    public String saveInvoice(@RequestParam Long customerId,
                              @RequestParam Long userId,
                              @ModelAttribute("invoiceItems") List<InvoiceItem> items,
                              Model model) {
        if (items.isEmpty()) {
            model.addAttribute("error", "Cannot save invoice with no items");
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            return "invoice/new";
        }

        Invoice invoice = invoiceService.createInvoice(customerId, userId, items);
        return "redirect:/invoices/" + invoice.getInvoiceId(); // Show saved invoice
    }

    // ✅ Show saved invoice (with QR code)
    @GetMapping("/{id}")
    public String showInvoice(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        try {
            // ✅ Generate SMALL UPI Payment QR (150x150) for screen + receipt
            String upiId = "yourpaytmid@upi"; // ✅ REPLACE WITH YOUR PAYTM UPI ID
            String merchantName = "K V H nextGen Store"; // ✅ REPLACE WITH YOUR STORE NAME
            String qrBase64Small = QRCodeGenerator.generateUpiPaymentQrBase64(
                    upiId, merchantName, invoice.getTotalAmt(), 150);

            model.addAttribute("qrBase64Small", qrBase64Small);
        } catch (Exception e) {
            model.addAttribute("qrError", "Failed to generate payment QR code");
            e.printStackTrace();
        }

        model.addAttribute("invoice", invoice);
        
        return "invoice/view"; // templates/invoice/view.html
    }

    // ✅ Show all invoices (with filters)
    @GetMapping
    public String listInvoices(Model model,
                               @RequestParam(required = false) LocalDate date,
                               @RequestParam(required = false) String status) {
        List<Invoice> invoices;
        if (date != null) {
            invoices = invoiceService.getInvoicesByDate(date);
        } else if (status != null && !status.isEmpty()) {
            invoices = invoiceService.getAllInvoices().stream()
                    .filter(inv -> status.equals(inv.getPaymentStatus()))
                    .toList();
        } else {
            invoices = invoiceService.getAllInvoices();
        }
        model.addAttribute("invoices", invoices);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedStatus", status);
        return "invoice/list"; // templates/invoice/list.html
    }

    
    // ✅ Mark invoice as paid
    @GetMapping("/pay/{id}")
    public String markAsPaid(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        // ✅ FIXED: Use PaymentStatus.PAID (enum) instead of "PAID" (string)
        invoiceService.updatePaymentStatus(invoice.getInvoiceId(), Invoice.PaymentStatus.PAID);
        
        return "redirect:/invoices/" + id;
    }
}