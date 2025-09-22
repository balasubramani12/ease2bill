package com.bsm.ease2bill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // ✅ Custom Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; // templates/auth/login.html
    }
}