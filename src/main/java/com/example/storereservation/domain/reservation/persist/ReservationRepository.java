package com.example.storereservation.domain.reservation.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUserIdOrderByTime(String userId);
    List<ReservationEntity> findByStoreNameOrderByTime(String storeName);
}
