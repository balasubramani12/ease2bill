package com.bsm.ease2bill.controller;

import com.bsm.ease2bill.entity.Invoice;
import com.bsm.ease2bill.service.InvoiceService;
import com.bsm.ease2bill.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private InvoiceService invoiceService;

    // ✅ Generate Thermal Receipt (Narrow, Minimal)
    @GetMapping("/thermal/{id}")
    public String generateThermalReceipt(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Generate SMALL UPI QR for receipt (120x120)
        try {
            String upiId = "yourpaytmid@upi"; // ✅ REPLACE WITH YOUR UPI ID
            String merchantName = "Ease2Bill Store"; // ✅ REPLACE WITH YOUR STORE NAME
            String qrBase64 = QRCodeGenerator.generateUpiPaymentQrBase64(
                    upiId, merchantName, invoice.getTotalAmt(), 120);
            model.addAttribute("qrBase64", qrBase64);
        } catch (Exception e) {
            model.addAttribute("qrError", "QR generation failed");
        }

        model.addAttribute("invoice", invoice);
        return "receipt/thermal"; // templates/receipt/thermal.html
    }

    // ✅ Generate A4 Receipt (Professional Layout)
    @GetMapping("/a4/{id}")
    public String generateA4Receipt(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Generate SMALL UPI QR for receipt (120x120)
        try {
            String upiId = "yourpaytmid@upi"; // ✅ REPLACE WITH YOUR UPI ID
            String merchantName = "Ease2Bill Store"; // ✅ REPLACE WITH YOUR STORE NAME
            String qrBase64 = QRCodeGenerator.generateUpiPaymentQrBase64(
                    upiId, merchantName, invoice.getTotalAmt(), 120);
            model.addAttribute("qrBase64", qrBase64);
        } catch (Exception e) {
            model.addAttribute("qrError", "QR generation failed");
        }

        model.addAttribute("invoice", invoice);
        return "receipt/a4"; // templates/receipt/a4.html
    }
}