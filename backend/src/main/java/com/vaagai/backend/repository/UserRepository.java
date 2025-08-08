package com.vaagai.backend.repository;

import com.vaagai.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	

	User findOneByEmailIgnoreCase(String email);
}
