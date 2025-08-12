package com.vaagaicart.backend.controller;



import com.vaagaicart.backend.dto.OrderDTO;
import com.vaagaicart.backend.entity.Order;
import com.vaagaicart.backend.exception.ResourceNotFoundException;
import com.vaagaicart.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST /order/new
    @PostMapping("/order/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
        Order createdOrder = orderService.createOrder(orderDTO, principal.getName());
        return ResponseEntity.ok(createdOrder);
    }


 // GET /order/{id}
    @GetMapping("/order/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, Principal principal) {
    	System.out.println("Principal username: " + principal.getName());

        Order order = orderService.getOrderById(id);

        if (order == null) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        } else {
            return ResponseEntity.ok(order);
        }
    }


    // GET /myorders
    @GetMapping("/myorders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        List<Order> orders = orderService.getOrdersByUser(principal.getName());
        return ResponseEntity.ok(orders);
    }

    // ADMIN ROUTES

    // GET /admin/orders
    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // PUT /admin/order/{id}
    @PutMapping("/admin/order/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Order orderUpdate) {
        Order updatedOrder = orderService.updateOrderStatus(id, orderUpdate.getOrderStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    // DELETE /admin/order/{id}
    @DeleteMapping("/admin/order/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}

