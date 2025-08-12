package com.vaagaicart.backend.service;



import com.vaagaicart.backend.dto.ProductDTO;
import com.vaagaicart.backend.dto.ProductRequest;
import com.vaagaicart.backend.dto.ReviewDTO;
import com.vaagaicart.backend.dto.ReviewRequest;
import com.vaagaicart.backend.entity.APIResponse;
import com.vaagaicart.backend.entity.Product;
import com.vaagaicart.backend.entity.Review;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



import java.util.List;

public interface ProductService {

    // GET all products (with pagination, search, and sorting defaults)
    List<Product> getProducts();

    // GET single product
    ProductDTO getSingleProduct(Long id);
    public Page<Product> getProductsPaged(int page, int size);

    // CREATE review
    void createReview(ReviewRequest reviewRequest);

    // CREATE product (Admin)
    Product newProduct(ProductRequest productRequest, MultipartFile[] images);

    // GET products for Admin
    List<Product> getAdminProducts();

    // DELETE product
    void deleteProduct(Long id);

    // UPDATE product
    Product updateProduct(Long id, ProductRequest productRequest, MultipartFile[] images);

    // GET reviews for a product
    List<ReviewDTO> getReviews(Long productId);

    // DELETE review
    void deleteReview(Long productId, Long reviewId);
    Page<Product> getFilteredProducts(int page, int size, String keyword, String category, Double rating, Double priceGte, Double priceLte);
}

