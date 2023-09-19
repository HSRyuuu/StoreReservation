package com.example.storereservation.domain.partner.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    boolean existsByPartnerId(String partnerId);
    Optional<PartnerEntity> findByPartnerId(String partnerId);
}
