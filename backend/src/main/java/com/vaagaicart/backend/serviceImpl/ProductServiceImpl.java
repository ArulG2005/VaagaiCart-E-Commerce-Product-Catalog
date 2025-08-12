package com.vaagaicart.backend.serviceImpl;

import com.vaagaicart.backend.dto.ProductDTO;
import com.vaagaicart.backend.dto.ProductImageDTO;
import com.vaagaicart.backend.dto.ProductRequest;
import com.vaagaicart.backend.dto.ReviewDTO;
import com.vaagaicart.backend.dto.ReviewRequest;
import com.vaagaicart.backend.dto.UserDTO;
import com.vaagaicart.backend.entity.Product;
import com.vaagaicart.backend.entity.ProductImage;
import com.vaagaicart.backend.entity.Review;
import com.vaagaicart.backend.entity.User;
import com.vaagaicart.backend.repository.ProductRepository;
import com.vaagaicart.backend.repository.ReviewRepository;
import com.vaagaicart.backend.repository.UserRepository;
import com.vaagaicart.backend.service.ProductService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/product/";

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public Page<Product> getFilteredProducts(int page, int size, String keyword, String category, Double rating, Double priceGte, Double priceLte) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Product> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }
        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }
        if (rating != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("ratings"), rating));
        }
        if (priceGte != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), priceGte));
        }
        if (priceLte != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), priceLte));
        }

        return productRepository.findAll(spec, pageable);
    }


    @Override
    public Page<Product> getProductsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }
    
    @Override
    public ProductDTO getSingleProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setRatings(product.getRatings());
        productDTO.setCategory(product.getCategory());
        productDTO.setSeller(product.getSeller());
        productDTO.setStock(product.getStock());
        productDTO.setNumOfReviews(product.getNumOfReviews());
        productDTO.setCreatedAt(product.getCreatedAt());
        
        // Convert ProductImage entities to DTOs
        List<ProductImageDTO> imageDTOs = product.getImages().stream()
            .map(img -> {
                ProductImageDTO dto = new ProductImageDTO();
                dto.setId(img.getId());
                dto.setFileName(img.getFileName());
                dto.setImageUrl(img.getImageUrl());
                return dto;
            })
            .collect(Collectors.toList());
        productDTO.setImages(imageDTOs);
        
        // Convert Review entities to ReviewDTOs (assuming you have a method for this)
        List<ReviewDTO> reviewDTOs = convertToReviewDTOs(product.getReviews());
        productDTO.setReviews(reviewDTOs);
        
        // Set user as null or UserDTO if needed
        productDTO.setUser(null);
        
        return productDTO;
    }
    public List<ReviewDTO> convertToReviewDTOs(List<Review> reviews) {
        if (reviews == null) return List.of(); // return empty list if null

        return reviews.stream().map(review -> {
            User user = review.getUser();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());

            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setId(review.getId());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setComment(review.getComment());
            reviewDTO.setUser(userDTO);

            return reviewDTO;
        }).collect(Collectors.toList());
    }

//    public Page<Product> getProducts(int page, int size, String keyword, Double priceGte, Double priceLte, String category, Double ratings) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
//
//        // Fetch all products paged
//        Page<Product> allProductsPage = productRepository.findAll(pageable);
//
//        // Filter manually in memory (not efficient for large data, but simple)
//        List<Product> filtered = allProductsPage.getContent().stream()
//                .filter(p -> keyword == null || p.getName().toLowerCase().contains(keyword.toLowerCase()))
//                .filter(p -> priceGte == null || p.getPrice() >= priceGte)
//                .filter(p -> priceLte == null || p.getPrice() <= priceLte)
//                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
//                .filter(p -> ratings == null || p.getRatings() >= ratings)
//                .collect(Collectors.toList());
//
//        // Return a new Page with filtered content (simple way)
//        return new PageImpl<>(filtered, pageable, allProductsPage.getTotalElements());
//    }

    @Override
    public void createReview(ReviewRequest reviewRequest) {
        // Get the authenticated user's email from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // assuming email is the username

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + userEmail);
        }


        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + reviewRequest.getProductId()));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        reviewRepository.save(review);
        
        
        List<Review> reviews = reviewRepository.findByProductId(product.getId());

        int numOfReviews = reviews.size();
        double avgRating = reviews.stream()
                                  .mapToInt(Review::getRating)
                                  .average()
                                  .orElse(0.0);

        product.setNumOfReviews(numOfReviews);
        product.setRatings((int) avgRating);

        productRepository.save(product);
    }

    @Value("${file.upload-dir}")
    private String uploadDir; // Example in application.properties: file.upload-dir=D:/uploads

    @Override
    public Product newProduct(ProductRequest productRequest, MultipartFile[] images) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setSeller(productRequest.getSeller());
        product.setStock(productRequest.getStock() != null ? productRequest.getStock() : 0);

        List<ProductImage> productImages = new ArrayList<>();

        if (images != null) {
            for (MultipartFile file : images) {
                try {
                    // ✅ Ensure uploads/product folder exists inside the base uploadDir
                    File uploadPath = new File(uploadDir + "/product");
                    if (!uploadPath.exists()) {
                        uploadPath.mkdirs();
                    }

                    // ✅ Unique filename
                    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    File dest = new File(uploadPath, filename);

                    // ✅ Save file to disk
                    file.transferTo(dest);

                    // ✅ Create ProductImage entity
                    ProductImage img = new ProductImage();
                    img.setFileName(filename);
                    img.setImageUrl("/uploads/product/" + filename); // matches ResourceHandler mapping
                    img.setProduct(product);

                    productImages.add(img);

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
                }
            }
        }

        // ✅ Attach images to product (CascadeType.ALL in Product.images)
        product.setImages(productImages);

        // ✅ Save product (images saved automatically)
        return productRepository.save(product);
    }



    @Override
    public List<Product> getAdminProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    public Product getSingleProduct1(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }


    @Override
    public Product updateProduct(Long id, ProductRequest productRequest, MultipartFile[] images) {
        Product product = getSingleProduct1(id);

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());

        if (images != null && images.length > 0) {
            List<ProductImage> newImages = new ArrayList<>();
            for (MultipartFile file : images) {
                try {
                    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    File dest = new File(UPLOAD_DIR + filename);
                    dest.getParentFile().mkdirs();
                    file.transferTo(dest);

                    ProductImage img = new ProductImage();
                    img.setFileName(filename);
                    img.setProduct(product);
                    newImages.add(img);

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage());
                }
            }
            product.setImages(newImages);
        }

        return productRepository.save(product);
    }

    @Override
    public List<ReviewDTO> getReviews(Long productId) {
        Product product = getSingleProduct1(productId);
        List<Review> reviews = reviewRepository.findByProduct(product);

        return reviews.stream()
                      .map(this::convertToDto)
                      .collect(Collectors.toList());
    }
    private ReviewDTO convertToDto(Review review) {
        UserDTO userDto = new UserDTO(review.getUser().getId(), review.getUser().getName());
        return new ReviewDTO(
            review.getId(),
            review.getRating(),
            review.getComment(),
            userDto
        );
    }


    @Override
    public void deleteReview(Long productId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        if (!review.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Review does not belong to the specified product");
        }
        reviewRepository.delete(review);
    }
}