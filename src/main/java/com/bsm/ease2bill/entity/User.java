package com.bsm.ease2bill.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // ✅ FIXED: matches DB column "user_id" (was "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    // One to Many Join with Invoice Entity
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // ✅ FIXED: Simplified cascade + added orphanRemoval
    private List<Invoice> invoices = new ArrayList<>(); // ✅ FIXED: Initialize to avoid NPE

    public User() {}

    public User(String name, String role, String username, String password) {
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Invoice> getInvoices() { return invoices; }
    public void setInvoices(List<Invoice> invoices) { this.invoices = invoices; }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        invoice.setUser(this);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", role=" + role + ", username=" + username + "]";
    }
}