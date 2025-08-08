package com.vaagai.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vaagai.backend.dto.OrderRequest;
import com.vaagai.backend.entity.Order;
import com.vaagai.backend.entity.OrderItem;
import com.vaagai.backend.entity.ShippingInfo;
import com.vaagai.backend.entity.User;
import com.vaagai.backend.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // You will need a way to get current logged-in user, here assumed as parameter for simplicity
    // In real app, use SecurityContextHolder or @AuthenticationPrincipal for current user

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
        User user = orderRequest.getUser(); // get user info from request or session/auth
        List<OrderItem> items = orderRequest.getOrderItems();
        ShippingInfo shippingInfo = orderRequest.getShippingInfo();

        Order order = orderService.placeOrder(user, items, shippingInfo);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, @RequestBody User user) {
        Optional<Order> cancelledOrder = orderService.cancelOrder(orderId, user);
        if (cancelledOrder.isPresent()) {
            return ResponseEntity.ok(cancelledOrder.get());
        } else {
            return ResponseEntity.status(403).body("You cannot cancel this order");
        }
    }
}
