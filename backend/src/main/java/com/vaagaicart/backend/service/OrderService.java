package com.vaagaicart.backend.service;



import java.util.List;

import com.vaagaicart.backend.dto.OrderDTO;
import com.vaagaicart.backend.entity.Order;
import com.vaagaicart.backend.exception.ResourceNotFoundException;

import java.util.List;
import com.vaagaicart.backend.entity.Order;

public interface OrderService {

	

    Order getOrderById(Long id);

    List<Order> getOrdersByUserId(Long userId);

    // ✅ New method for fetching orders by username/email
    List<Order> getOrdersByUser(String usernameOrEmail);

    // ✅ New method for fetching a single order for a user
    Order getOrderByIdForUser(Long id, String usernameOrEmail);

    List<Order> getAllOrders();

    Order updateOrderStatus(Long id, String status);

    void deleteOrder(Long id);

     Order createOrder(OrderDTO orderDTO, String usernameOrEmail);
}

