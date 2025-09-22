package com.bsm.ease2bill.repository;

import com.bsm.ease2bill.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ✅ Find by name (partial, case-insensitive) — for product search in UI
    List<Product> findByNameContainingIgnoreCase(String name);

    // ✅ Find by category
    List<Product> findByProdCat(String category);

    // ✅ Find by UPC (barcode) — unique
    Optional<Product> findByProdSku(String upc);

    // ✅ Find by name + category
    List<Product> findByNameContainingIgnoreCaseAndProdCatContainingIgnoreCase(String name, String category);
}