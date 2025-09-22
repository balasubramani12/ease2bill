package com.bsm.ease2bill.repository;

import com.bsm.ease2bill.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Custom method: Find user by username (for login)
    Optional<User> findByUsername(String username);
}