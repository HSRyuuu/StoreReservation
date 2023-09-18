package com.example.storereservation.partner.repository;

import com.example.storereservation.partner.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    boolean existsByPartnerId(String partnerId);
    Optional<PartnerEntity> findByPartnerId(String partnerId);
}
