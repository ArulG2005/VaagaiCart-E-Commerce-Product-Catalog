package com.vaagaicart.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaagaicart.backend.entity.Product;
import com.vaagaicart.backend.entity.Review;
import com.vaagaicart.backend.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {



	List<Review> findByProduct(Product product);

	List<Review> findByProductId(Long id);
    
}
