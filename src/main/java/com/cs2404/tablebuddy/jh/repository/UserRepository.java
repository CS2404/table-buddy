package com.cs2404.tablebuddy.jh.repository;

import com.cs2404.tablebuddy.jh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
