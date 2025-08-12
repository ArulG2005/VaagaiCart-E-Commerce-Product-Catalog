package com.vaagaicart.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import com.vaagaicart.backend.dto.ProductDTO;
import com.vaagaicart.backend.dto.ProductRequest;
import com.vaagaicart.backend.dto.ReviewDTO;
import com.vaagaicart.backend.dto.ReviewRequest;
import com.vaagaicart.backend.entity.APIResponse;
import com.vaagaicart.backend.entity.Product;
import com.vaagaicart.backend.entity.Review;
import com.vaagaicart.backend.service.ProductService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ---------------- Public Routes ----------------

    
    
    @GetMapping("/productsF")
    public ResponseEntity<?> getProducts(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "3") int size,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "ratings", required = false) Double rating,
        @RequestParam(value = "price[gte]", required = false) Double priceGte,
        @RequestParam(value = "price[lte]", required = false) Double priceLte
    ) {
        // Call your service layer to get filtered products
        Page<Product> products = productService.getFilteredProducts(page, size, keyword, category, rating, priceGte, priceLte);

        return ResponseEntity.ok(products);
    }
    @GetMapping("/products")
    public ResponseEntity<?> getProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double ratings,
            @RequestParam(value = "priceGte", required = false) Double priceGte,
            @RequestParam(value = "priceLte", required = false) Double priceLte) {

        Page<Product> productPage = productService.getFilteredProducts(page, size, keyword, category, ratings, priceGte, priceLte);

        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("count", productPage.getTotalElements());
        response.put("resPerPage", productPage.getSize());

        return ResponseEntity.ok(response);
    }



    

    // GET /product/{id}
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getSingleProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getSingleProduct(id));
    }

    // PUT /review  (Authenticated User)
    @PutMapping("/review")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest reviewRequest) {
        productService.createReview(reviewRequest);
        return ResponseEntity.ok("Review created successfully");
    }

    // ---------------- Admin Routes ----------------

    // POST /admin/product/new
    @PostMapping("/admin/product/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> newProduct(
            @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {
        return ResponseEntity.ok(productService.newProduct(productRequest, images));
    }

    // GET /admin/products
    @GetMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> getAdminProducts() {
        return ResponseEntity.ok(productService.getAdminProducts());
    }

    // DELETE /admin/product/{id}
    @DeleteMapping("/admin/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // PUT /admin/product/{id}
    @PutMapping("/admin/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest, images));
    }

    @GetMapping("/admin/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDTO>> getReviews(@RequestParam Long productId) {
        List<ReviewDTO> reviewDTOs = productService.getReviews(productId);
        return ResponseEntity.ok(reviewDTOs);
    }


    // DELETE /admin/review
    @DeleteMapping("/admin/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReview(@RequestParam Long productId, @RequestParam Long reviewId) {
        productService.deleteReview(productId, reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}