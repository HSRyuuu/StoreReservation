package com.example.storereservation.domain.review.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    boolean existsByReservationId(Long reservationId);
    Page<ReviewEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    Page<ReviewEntity> findByStoreNameOrderByCreatedAtDesc(String storeName, Pageable pageable);
    Page<ReviewEntity> findByStoreNameOrderByRatingDesc(String storeName, Pageable pageable);
    Page<ReviewEntity> findByStoreNameOrderByRatingAsc(String storeName, Pageable pageable);
}
