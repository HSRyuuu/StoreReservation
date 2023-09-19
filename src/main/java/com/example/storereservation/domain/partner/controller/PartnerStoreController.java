package com.example.storereservation.domain.partner.controller;

import com.example.storereservation.domain.partner.dto.EditStore;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.service.PartnerStoreService;
import com.example.storereservation.domain.partner.dto.AddStore;
import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PartnerStoreController {

    private final PartnerStoreService partnerStoreService;

    /**
     * 상점 등록
     * @param partnerId : 파트너 ID
     * @param request   : 상점 정보 입력
     */
    @PostMapping("/partner/register-store/{partnerId}")
    public ResponseEntity<?> registerStore(@PathVariable String partnerId,
                                           @RequestBody AddStore.Request request) {
        StoreDto savedStore = partnerStoreService.addStore(partnerId, request);
        return ResponseEntity.ok(AddStore.Response.fromDto(savedStore));
    }

    /**
     * 상점 수정
     * @Param partnerId : 파트너 ID
     * @Param
     */
    @PutMapping("/partner/edit-store/{partnerId}")
    public ResponseEntity<?> editStore(@PathVariable String partnerId,
                                       @RequestBody EditStore.Request request,
                                       @AuthenticationPrincipal PartnerEntity partner) {
        if(!partnerId.equals(partner.getPartnerId())){
            throw new MyException(ErrorCode.STORE_PARTNER_NOT_MATCH);
        }
        StoreDto storeDto = partnerStoreService.editStore(partnerId, request);
        return ResponseEntity.ok(EditStore.Response.fromDto(storeDto));
    }
}
