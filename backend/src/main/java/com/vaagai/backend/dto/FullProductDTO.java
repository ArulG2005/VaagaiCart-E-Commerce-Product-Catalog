package com.vaagai.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.vaagai.backend.entity.Product.Category;

public class FullProductDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private BigDecimal ratings;
    private Category category;
    private String seller;
    private Integer stock;
    private Integer numOfReviews;
    private List<String> productImages;
    private List<ProductReviewDTO> productReviews;

    // getters and setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getRatings() { return ratings; }
    public void setRatings(BigDecimal bigDecimal) { this.ratings = bigDecimal; }

    public Category getCategory() { return category; }
    public void setCategory(Category category2) { this.category = category2; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getNumOfReviews() { return numOfReviews; }
    public void setNumOfReviews(Integer numOfReviews) { this.numOfReviews = numOfReviews; }

    public List<String> getProductImages() { return productImages; }
    public void setProductImages(List<String> productImages) { this.productImages = productImages; }

    public List<ProductReviewDTO> getProductReviews() { return productReviews; }
    public void setProductReviews(List<ProductReviewDTO> productReviews) { this.productReviews = productReviews; }
}