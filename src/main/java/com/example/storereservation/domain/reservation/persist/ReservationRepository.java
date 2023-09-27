package com.example.storereservation.domain.reservation.persist;

import com.example.storereservation.domain.reservation.type.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    Page<ReservationEntity> findByUserIdOrderByTime(String userId, Pageable pageable);
    Page<ReservationEntity> findByUserIdAndStatusOrderByTime(String userId, ReservationStatus status, Pageable pageable);
    Page<ReservationEntity> findByPartnerIdOrderByTime(String partnerId, Pageable pageable);
    Page<ReservationEntity> findByPartnerIdAndStatusOrderByTime(String partnerId, ReservationStatus status, Pageable pageable);
}
