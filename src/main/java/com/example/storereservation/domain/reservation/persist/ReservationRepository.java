package com.example.storereservation.domain.reservation.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUserIdOrderByTime(String userId);
    Page<ReservationEntity> findByPartnerIdOrderByTime(String partnerId, Pageable pageable);
}
