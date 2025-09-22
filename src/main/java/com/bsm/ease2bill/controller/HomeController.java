package com.bsm.ease2bill.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ✅ Show Dashboard (after login)
    /*
    @GetMapping("/")
    public String showDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            model.addAttribute("role", authentication.getAuthorities().toString());
        }
        return "home/dashboard"; // templates/home/dashboard.html
    }
     */
    @GetMapping("/")
    public String showDashboard(Model model) {
        // ✅ Remove Authentication check — just show dashboard
        model.addAttribute("username", "Guest");
        model.addAttribute("role", "USER");
        return "home/dashboard";
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // ✅ Check for ROLE_ADMIN (Spring adds prefix automatically)
            if (authentication.getAuthorities().toString().contains("ROLE_ADMIN")) {
                return "redirect:/products";
            } else {
                return "redirect:/invoices/new";
            }
        }
        return "redirect:/login?error";
    }

    // ✅ Access Denied page
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied"; // templates/error/access-denied.html
    }
}