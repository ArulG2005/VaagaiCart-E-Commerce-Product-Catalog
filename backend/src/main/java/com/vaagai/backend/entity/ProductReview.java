package com.vaagai.backend.entity;

import jakarta.persistence.Entity;


import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_reviews")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public BigDecimal rating;
    public String comment;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
       
}
