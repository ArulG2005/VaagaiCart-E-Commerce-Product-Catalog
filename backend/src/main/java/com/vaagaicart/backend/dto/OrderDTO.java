package com.vaagaicart.backend.dto;

import java.util.List;

import com.vaagaicart.backend.entity.PaymentInfo;

public class OrderDTO {
	 private Long userId;
    private List<OrderItemDTO> orderItems;
    private ShippingInfoDTO shippingInfo;
    private PaymentInfo paymentInfo;
    private Double itemsPrice;
    private Double shippingPrice;
    private Double taxPrice;
    private Double totalPrice;

    // Getters and Setters

    
    
    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public ShippingInfoDTO getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoDTO shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public Double getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(Double itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public Double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public Double getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(Double taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Nested DTO classes (or put them in separate files)

    public static class OrderItemDTO {
        private String name;
        private Integer quantity;
        private String image;
        private Double price;
        private Long productId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
    }

    public static class ShippingInfoDTO {
        private String city;
        private String postalCode;
        private String country;
        private String state;
        private String address;
        private String phoneNo;

        // Getters and setters
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhoneNo() { return phoneNo; }
        public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }
    }
}
