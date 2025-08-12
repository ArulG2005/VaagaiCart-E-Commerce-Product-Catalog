package com.vaagaicart.backend.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- This is the key fix
	    @Column(name = "images_id")
	    private Long id;
    
    private String fileName;
    private String imageUrl; 
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Transient // not stored in DB
    public String getImageUrl() {
        return "http://localhost:8080/uploads/product/" + fileName; 
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


}
