package com.vaagaicart.backend.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaagaicart.backend.dto.OrderDTO;
import com.vaagaicart.backend.dto.OrderDTO.ShippingInfoDTO;
import com.vaagaicart.backend.entity.Order;
import com.vaagaicart.backend.entity.OrderItem;
import com.vaagaicart.backend.entity.PaymentInfo;
import com.vaagaicart.backend.entity.Product;
import com.vaagaicart.backend.entity.ShippingInfo;
import com.vaagaicart.backend.entity.User;
import com.vaagaicart.backend.exception.ResourceNotFoundException;
import com.vaagaicart.backend.repository.OrderRepository;
import com.vaagaicart.backend.repository.ProductRepository;
import com.vaagaicart.backend.repository.UserRepository;
import com.vaagaicart.backend.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Inject all required repositories via constructor
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

   

    @Transactional
    @Override
    public Order createOrder(OrderDTO orderDTO, String usernameOrEmail) throws ResourceNotFoundException {
        // 1. Find the user
        User user = userRepository.findByEmail(usernameOrEmail);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + usernameOrEmail);
        }

        // 2. Create the order
        Order order = new Order();
        order.setUser(user);
        order.setShippingInfo(mapToShippingInfo(orderDTO.getShippingInfo(), user));
        order.setPaidAt(new Date());
        PaymentInfo paymentInfo = new PaymentInfo(orderDTO.getPaymentInfo().getId(), orderDTO.getPaymentInfo().getStatus());
        order.setPaymentInfo(paymentInfo);

        // 3. Map and validate order items
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            List<OrderItem> orderItems = new ArrayList<>();

            for (OrderDTO.OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                if (itemDTO.getProductId() == null) {
                    throw new IllegalArgumentException("Product id is required for all order items");
                }

                // Fetch product
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));

                // Check stock availability
                if (product.getStock() < itemDTO.getQuantity()) {
                    throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
                }

                // Reduce stock
                product.setStock(product.getStock() - itemDTO.getQuantity());
                productRepository.save(product);

                // Create order item
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                item.setPrice(itemDTO.getPrice());
                item.setOrder(order);

                orderItems.add(item);
            }

            order.setOrderItems(orderItems);
        } else {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        // 4. Set prices
        order.setItemsPrice(orderDTO.getItemsPrice());
        order.setShippingPrice(orderDTO.getShippingPrice());
        order.setTaxPrice(orderDTO.getTaxPrice());
        order.setTotalPrice(orderDTO.getTotalPrice());

        // 5. Save and return the order
        return orderRepository.save(order);
    }

    private ShippingInfo mapToShippingInfo(OrderDTO.ShippingInfoDTO dto, User user) {
        if (dto == null) return null;
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setAddress(dto.getAddress());
        shippingInfo.setCity(dto.getCity());
        shippingInfo.setCountry(dto.getCountry());
        shippingInfo.setPostalCode(dto.getPostalCode());
        shippingInfo.setPhoneNo(dto.getPhoneNo());
        shippingInfo.setState(dto.getState());
        shippingInfo.setName(user.getName());
        return shippingInfo;
    }


	@Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByUser(String usernameOrEmail) {
        User user = userRepository.findByEmail(usernameOrEmail);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + usernameOrEmail);
        }
        return orderRepository.findByUserId(user.getId());
    }

    @Override
    public Order getOrderByIdForUser(Long id, String usernameOrEmail) {
        User user = userRepository.findByEmail(usernameOrEmail);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + usernameOrEmail);
        }

        Order order = orderRepository.findByIdAndUserId(id, user.getId());
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + id + " for user");
        }

        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setOrderStatus(status);
        if ("Delivered".equalsIgnoreCase(status)) {
            order.setDeliveredAt(new java.util.Date());
        }
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }



	
}
