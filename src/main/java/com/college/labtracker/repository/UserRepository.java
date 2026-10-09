package com.college.labtracker.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.User;

// Repository interface for database CRUD operations on User entity
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their unique roll number or staff ID
    Optional<User> findByUsername(String username);

    // Check if a username is already taken
    boolean existsByUsername(String username);
}
