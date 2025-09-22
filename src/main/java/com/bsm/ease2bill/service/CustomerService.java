package com.bsm.ease2bill.service;

import com.bsm.ease2bill.entity.Customer;
import com.bsm.ease2bill.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // ✅ Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ Get customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // ✅ Get customer by phone
    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    // ✅ Get customer by email
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // ✅ Search customers by name (partial, case-insensitive)
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    // ✅ Save or update customer
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // ✅ Delete customer by ID
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // ✅ Check if phone already exists (for validation)
    public boolean existsByPhone(String phone) {
        return customerRepository.findByPhone(phone).isPresent();
    }

    // ✅ Check if email already exists
    public boolean existsByEmail(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }
}