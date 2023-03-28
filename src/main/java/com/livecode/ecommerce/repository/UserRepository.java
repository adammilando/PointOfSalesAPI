package com.livecode.ecommerce.repository;

import com.livecode.ecommerce.model.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
