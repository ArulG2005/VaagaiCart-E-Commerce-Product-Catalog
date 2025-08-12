package com.vaagaicart.backend.dto;

import java.util.List;

import com.vaagaicart.backend.entity.ProductImage;

public class ProductRequest {
	 private String name;
	    private String description;
	    private Double price;
	    private String category;
	    private String seller;
	    private Integer stock;

	
	  // add this

	    // getters and setters for all fields including seller and stock
	    public String getSeller() { return seller; }
	    public void setSeller(String seller) { this.seller = seller; }

	    public Integer getStock() { return stock; }
	    public void setStock(Integer stock) { this.stock = stock; }
    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
 
    
   
}
