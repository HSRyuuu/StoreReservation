package com.example.storereservation.user.repository;

import com.example.storereservation.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUserId(String userId);
    Optional<UserEntity> findByUserId(String userId);
}
