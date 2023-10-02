package com.example.storereservation.domain.store.persist;

import com.example.storereservation.domain.store.persist.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    boolean existsByPartnerId(String partnerId);
    boolean existsByStoreName(String storeName);
    Optional<StoreEntity> findByStoreName(String storeName);
    Optional<StoreEntity> findByPartnerId(String partnerId);
    Page<StoreEntity> findByStoreNameContaining(String storeName, Pageable pageable);

}
