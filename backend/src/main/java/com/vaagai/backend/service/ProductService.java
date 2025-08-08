package com.vaagai.backend.service;

import com.vaagai.backend.dto.FullProductDTO;
import com.vaagai.backend.dto.ProductReviewDTO;
import com.vaagai.backend.dto.ShortProductDTO;
import com.vaagai.backend.entity.Product;
import com.vaagai.backend.entity.ProductImage;
import com.vaagai.backend.entity.ProductReview;
import com.vaagai.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<ShortProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> new ShortProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getSeller()
                ));
    }

    public FullProductDTO getProductById(Integer id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));

        FullProductDTO dto = new FullProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setRatings(product.getRatings());
        dto.setCategory(product.getCategory());
        dto.setSeller(product.getSeller());
        dto.setStock(product.getStock());
        dto.setNumOfReviews(product.getNumOfReviews());

        List<String> images = product.getProductImages().stream()
                .map(ProductImage::getImage)
                .collect(Collectors.toList());
        dto.setProductImages(images);

        List<ProductReviewDTO> reviews = product.getProductReviews().stream()
                .map(r -> new ProductReviewDTO(
                        r.getRating(),
                        r.getComment(),
                        r.getUser().getName() // assuming User has getName()
                )).collect(Collectors.toList());
        dto.setProductReviews(reviews);

        return dto;
    }
}

