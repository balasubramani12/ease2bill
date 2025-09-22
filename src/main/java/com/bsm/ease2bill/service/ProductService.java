package com.bsm.ease2bill.service;

import com.bsm.ease2bill.entity.Product;
import com.bsm.ease2bill.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ✅ Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // ✅ Get product by UPC (barcode)
    public Optional<Product> getProductBySku(String upc) {
        return productRepository.findByProdSku(upc);
    }

    // ✅ Search products by name (partial, case-insensitive)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // ✅ Search by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByProdCat(category);
    }

    // ✅ Search by name + category
    public List<Product> searchProductsByNameAndCategory(String name, String category) {
        return productRepository.findByNameContainingIgnoreCaseAndProdCatContainingIgnoreCase(name, category);
    }

    // ✅ Save or update product
    @Transactional
    public Product saveProduct(Product product) {
        // Optional: Validate that selling price <= MRP
        if (product.getProdSellingPrice().compareTo(product.getProdMrp()) > 0) {
            throw new IllegalArgumentException("Selling price cannot exceed MRP");
        }
        return productRepository.save(product);
    }

    // ✅ Delete product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // ✅ Check if UPC already exists (for validation)
    public boolean existsBySku(String upc) {
        return productRepository.findByProdSku(upc).isPresent();
    }

    // ✅ Reduce stock when item is sold (called from InvoiceService)
    @Transactional
    public void reduceStock(Long prodId, BigDecimal quantitySold) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + prodId));

        BigDecimal newStock = product.getStock().subtract(quantitySold);
        if (newStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        product.setStock(newStock);
        productRepository.save(product);
    }
}