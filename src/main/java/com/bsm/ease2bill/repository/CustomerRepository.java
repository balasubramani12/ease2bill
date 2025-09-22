package com.bsm.ease2bill.repository;

import com.bsm.ease2bill.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // ✅ Find customer by phone (unique)
    Optional<Customer> findByPhone(String phone);

    // ✅ Find customer by email (unique)
    Optional<Customer> findByEmail(String email);

    // ✅ Find by name (partial match) — for search UI
    List<Customer> findByNameContainingIgnoreCase(String name);
}