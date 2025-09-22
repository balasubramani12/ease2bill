package com.bsm.ease2bill.controller;

import com.bsm.ease2bill.entity.Customer;
import com.bsm.ease2bill.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ✅ Show all customers
    @GetMapping
    public String listCustomers(Model model, 
                                @RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("customers", customerService.searchCustomersByName(search));
            model.addAttribute("searchTerm", search);
        } else {
            model.addAttribute("customers", customerService.getAllCustomers());
        }
        return "customer/list"; // templates/customer/list.html
    }

    // ✅ Show new customer form
    @GetMapping("/new")
    public String showNewCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/form"; // templates/customer/form.html (used for new + edit)
    }

    // ✅ Handle create customer
    @PostMapping("/new")
    public String createCustomer(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult result,
                                 Model model) {
        // Validate unique phone
        if (customerService.existsByPhone(customer.getPhone())) {
            result.rejectValue("phone", "error.customer", "Phone number already exists");
        }

        // Validate unique email (if provided)
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty() &&
            customerService.existsByEmail(customer.getEmail())) {
            result.rejectValue("email", "error.customer", "Email already exists");
        }

        if (result.hasErrors()) {
            return "customer/form";
        }

        customerService.saveCustomer(customer);
        return "redirect:/customers?success";
    }

    // ✅ Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        model.addAttribute("customer", customer);
        return "customer/form"; // Reuse same form template
    }

    // ✅ Handle update
    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "customer/form";
        }

        customer.setCustId(id); // Preserve ID
        customerService.saveCustomer(customer);
        return "redirect:/customers?updated";
    }

    // ✅ Delete customer
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers?deleted";
    }
}