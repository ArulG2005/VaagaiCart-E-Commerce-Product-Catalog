package com.vaagai.backend.dto;

import java.math.BigDecimal;

public class ShortProductDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String seller;

    public ShortProductDTO(Integer id, String name, BigDecimal price, String seller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.seller = seller;
    }
    // getters and setters...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }
}