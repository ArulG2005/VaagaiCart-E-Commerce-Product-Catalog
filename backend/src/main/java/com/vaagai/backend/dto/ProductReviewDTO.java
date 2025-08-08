package com.vaagai.backend.dto;

import java.math.BigDecimal;

public class ProductReviewDTO {
    private BigDecimal rating;
    private String comment;
    private String userName;

    public ProductReviewDTO(BigDecimal rating, String comment, String userName) {
        this.rating = rating;
        this.comment = comment;
        this.userName = userName;
    }

    // getters/setters
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}