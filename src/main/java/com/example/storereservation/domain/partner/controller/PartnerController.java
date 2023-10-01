package com.example.storereservation.domain.partner.controller;

import com.example.storereservation.domain.partner.dto.AddStore;
import com.example.storereservation.domain.partner.dto.EditStore;
import com.example.storereservation.domain.partner.dto.PartnerDto;
import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.service.PartnerService;
import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PartnerController {

    private final PartnerService partnerService;

    /**
     * 파트너 회원가입
     */
    @PostMapping("/partner/register")
    public ResponseEntity<?> registerPartner(@RequestBody RegisterPartner.Request request) {
        PartnerDto registeredManager = partnerService.register(request);

        return ResponseEntity.ok(RegisterPartner.Response.fromDto(registeredManager));
    }

    /**
     * 매장 등록
     * @param partnerId : 파트너 ID
     * @param request   : 매장 정보 입력
     */
    @PostMapping("/partner/register-store/{partnerId}")
    public ResponseEntity<?> registerStore(@PathVariable String partnerId,
                                           @RequestBody AddStore.Request request) {
        StoreDto savedStore = partnerService.addStore(partnerId, request);
        return ResponseEntity.ok(AddStore.Response.fromDto(savedStore));
    }

    /**
     * 매장 정보 수정
     * @Param partnerId : 파트너 ID
     * @Param
     */
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @PutMapping("/partner/edit-store/{partnerId}")
    public ResponseEntity<?> editStore(@PathVariable String partnerId,
                                       @RequestBody EditStore.Request request,
                                       @AuthenticationPrincipal PartnerEntity partner) {
        if(!partnerId.equals(partner.getPartnerId())){
            throw new MyException(ErrorCode.STORE_PARTNER_NOT_MATCH);
        }
        StoreDto storeDto = partnerService.editStore(partnerId, request);
        return ResponseEntity.ok(EditStore.Response.fromDto(storeDto));
    }


}
