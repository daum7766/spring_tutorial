package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.User;
import java.util.Optional;

public interface UserDAO extends JpaRepository<User, String> {
    
    Optional<User> findUserByUidAndPassword(String uid, String password);

    Optional<User> findUserByEmail(String email);
}