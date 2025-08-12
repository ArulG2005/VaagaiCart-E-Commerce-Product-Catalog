package com.vaagaicart.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vaagaicart.backend.entity.Order;
import com.vaagaicart.backend.entity.Product;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

	List<Order> findByUserId(Long userId);
    // Custom queries if needed

	Order findByIdAndUserId(Long id, Long id2);
}