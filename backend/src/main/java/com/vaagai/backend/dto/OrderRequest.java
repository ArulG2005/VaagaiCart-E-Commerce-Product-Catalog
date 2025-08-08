package com.vaagai.backend.dto;

import com.vaagai.backend.dto.*;

import java.util.List;

import com.vaagai.backend.entity.OrderItem;
import com.vaagai.backend.entity.ShippingInfo;
import com.vaagai.backend.entity.User;

public class OrderRequest {
    private User user;
    private List<OrderItem> orderItems;
    private ShippingInfo shippingInfo;

    // getters and setters
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    public ShippingInfo getShippingInfo() {
        return shippingInfo;
    }
    public void setShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }
}
