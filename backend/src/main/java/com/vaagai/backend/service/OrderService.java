package com.vaagai.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaagai.backend.entity.Order;
import com.vaagai.backend.entity.OrderItem;
import com.vaagai.backend.entity.Product;
import com.vaagai.backend.entity.ShippingInfo;
import com.vaagai.backend.entity.User;
import com.vaagai.backend.repository.OrderRepository;
import com.vaagai.backend.repository.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(User user, List<OrderItem> orderItems, ShippingInfo shippingInfo) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus("Processing");
        order.setShippingInfo(shippingInfo);
        shippingInfo.setOrder(order);

        double itemsPrice = 0.0;

        for (OrderItem item : orderItems) {
            // Fetch product from DB to ensure it exists and to get managed entity
            Integer productId = item.getProduct().getId();
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

            item.setProduct(product); // set managed product entity
            item.setOrder(order); // set the owning order

            // Calculate total price for this item
            itemsPrice += item.getPrice().doubleValue() * item.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setItemsPrice(itemsPrice);

        // Set tax, shipping, and total price
        order.setTaxPrice(itemsPrice * 0.1);  // example 10% tax
        order.setShippingPrice(5.0);          // example fixed shipping fee
        order.setTotalPrice(order.getItemsPrice() + order.getTaxPrice() + order.getShippingPrice());

        return orderRepository.save(order);
    }

    public Optional<Order> cancelOrder(Integer orderId, User user) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Check if the order belongs to the user
            if (order.getUser().getId().equals(user.getId())) {
                order.setOrderStatus("Cancelled");
                orderRepository.save(order);
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }
}
