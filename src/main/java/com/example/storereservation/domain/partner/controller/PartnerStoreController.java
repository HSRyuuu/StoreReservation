package com.example.storereservation.domain.partner.controller;

import com.example.storereservation.domain.store.dto.AddStore;
import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PartnerStoreController {
    private final StoreService storeService;

    /**
     * 상점 등록
     *
     * @param partnerId : 파트너 ID
     * @param request   : 상점 정보 입력
     */
    @PostMapping("/partner/register-store/{partnerId}")
    public ResponseEntity<?> registerStore(@PathVariable String partnerId,
                                           @RequestBody AddStore.Request request) {
        StoreDto savedStore = storeService.addStore(partnerId, request);
        return ResponseEntity.ok(AddStore.Response.fromDto(savedStore));
    }
}
