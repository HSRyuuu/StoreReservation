package com.example.storereservation.domain.reservation.persist;

import com.example.storereservation.domain.reservation.type.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    Page<ReservationEntity> findByUserIdOrderByTimeDesc(String userId, Pageable pageable);

    Page<ReservationEntity> findByUserIdAndStatusOrderByTime(String userId, ReservationStatus status, Pageable pageable);

    Page<ReservationEntity> findByPartnerIdOrderByTimeDesc(String partnerId, Pageable pageable);

    Page<ReservationEntity> findByPartnerIdAndStatusOrderByTime(
            String partnerId, ReservationStatus status, Pageable pageable);

    Page<ReservationEntity> findByPartnerIdAndTimeBetweenOrderByTime(
            String partnerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<ReservationEntity> findByPartnerIdAndStatusAndTimeBetweenOrderByTime(
            String partnerId, ReservationStatus status, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
