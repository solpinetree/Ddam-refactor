package com.ddam.spring.repository;

import com.ddam.spring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);

	User findById(long id);
}
