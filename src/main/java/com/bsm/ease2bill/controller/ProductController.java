package com.bsm.ease2bill.controller;

import com.bsm.ease2bill.entity.Product;
import com.bsm.ease2bill.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ✅ Show all products + search
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String category) {
        if (name != null && !name.trim().isEmpty() && category != null && !category.trim().isEmpty()) {
            model.addAttribute("products", productService.searchProductsByNameAndCategory(name, category));
        } else if (name != null && !name.trim().isEmpty()) {
            model.addAttribute("products", productService.searchProductsByName(name));
        } else if (category != null && !category.trim().isEmpty()) {
            model.addAttribute("products", productService.getProductsByCategory(category));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }

        model.addAttribute("searchName", name);
        model.addAttribute("searchCategory", category);
        return "product/list"; // templates/product/list.html
    }

    // ✅ Show new product form
    @GetMapping("/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        product.setProdMrp(BigDecimal.ZERO);
        product.setProdSellingPrice(BigDecimal.ZERO);
        product.setStock(BigDecimal.ZERO);
        product.setOpeningStock(BigDecimal.ZERO);
        model.addAttribute("product", product);
        return "product/form"; // templates/product/form.html
    }

    // ✅ Handle create product
    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model) {
        // Validate unique UPC
        if (product.getProdSku() != null && !product.getProdSku().trim().isEmpty() &&
            productService.existsBySku(product.getProdSku())) {
            result.rejectValue("prodSku", "error.product", "UPC/SKU already exists");
        }

        if (result.hasErrors()) {
            return "product/form";
        }

        productService.saveProduct(product);
        return "redirect:/products?success";
    }

    // ✅ Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "product/form";
    }

    // ✅ Handle update
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "product/form";
        }

        product.setProdId(id); // Preserve ID
        productService.saveProduct(product);
        return "redirect:/products?updated";
    }

    // ✅ Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products?deleted";
    }
}