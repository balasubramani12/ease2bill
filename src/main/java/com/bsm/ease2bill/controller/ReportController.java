package com.bsm.ease2bill.controller;

import com.bsm.ease2bill.entity.Invoice;
import com.bsm.ease2bill.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private InvoiceService invoiceService;

    // ✅ Show Day-wise Report
    @GetMapping("/daily")
    public String showDailyReport(@RequestParam(required = false) String date,
                                  Model model) {
        LocalDate reportDate;
        if (date == null || date.trim().isEmpty()) {
            reportDate = LocalDate.now();
        } else {
            reportDate = LocalDate.parse(date);
        }

        List<Invoice> invoices = invoiceService.getInvoicesByDate(reportDate);

        // Calculate totals
        BigDecimal totalSales = invoices.stream()
                .map(Invoice::getTotalAmt)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSavings = invoices.stream()
                .map(Invoice::getTotalSaved)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("invoices", invoices);
        model.addAttribute("reportDate", reportDate);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalSavings", totalSavings);
        model.addAttribute("formattedDate", reportDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        return "report/daily"; // templates/report/daily.html
    }

    // ✅ Show Month-wise Report
    @GetMapping("/monthly")
    public String showMonthlyReport(@RequestParam(required = false) String monthYear,
                                    Model model) {
        YearMonth reportMonth;
        if (monthYear == null || monthYear.trim().isEmpty()) {
            reportMonth = YearMonth.now();
        } else {
            reportMonth = YearMonth.parse(monthYear);
        }

        LocalDate startDate = reportMonth.atDay(1);
        LocalDate endDate = reportMonth.atEndOfMonth();

        List<Invoice> invoices = invoiceService.getInvoicesBetweenDates(startDate, endDate);

        // Group by day
        Map<LocalDate, List<Invoice>> dailyGroups = invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getInvoiceDate));

        // Calculate daily totals
        Map<LocalDate, BigDecimal> dailySales = dailyGroups.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(Invoice::getTotalAmt)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ));

        Map<LocalDate, BigDecimal> dailySavings = dailyGroups.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(Invoice::getTotalSaved)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ));

        // Grand totals
        BigDecimal grandTotalSales = invoices.stream()
                .map(Invoice::getTotalAmt)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal grandTotalSavings = invoices.stream()
                .map(Invoice::getTotalSaved)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("dailyGroups", dailyGroups);
        model.addAttribute("dailySales", dailySales);
        model.addAttribute("dailySavings", dailySavings);
        model.addAttribute("reportMonth", reportMonth);
        model.addAttribute("grandTotalSales", grandTotalSales);
        model.addAttribute("grandTotalSavings", grandTotalSavings);
        model.addAttribute("formattedMonth", reportMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        return "report/monthly"; // templates/report/monthly.html
    }
}