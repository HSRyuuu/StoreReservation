package com.example.storereservation.web.controller;

import com.example.storereservation.partner.service.PartnerService;
import com.example.storereservation.partner.dto.PartnerDto;
import com.example.storereservation.partner.dto.RegisterPartner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    /**
     * 파트너 회원가입
     */
    @PostMapping("/register/partner")
    public ResponseEntity<?> registerPartner(@RequestBody RegisterPartner.Request request){
        PartnerDto registeredManager = partnerService.register(request);

        return ResponseEntity.ok(RegisterPartner.Response.fromDto(registeredManager));
    }


//    /**
//     * 예약 요청 내역확인
//     */
//    @GetMapping("/reservation/store/{storeId}")
//    public ResponseEntity<?> checkReservationRequest(@PathVariable String storeId){
//
//        return ResponseEntity.ok(null);
//    }
//
//    /**
//     * 예약 요청 승인 or 거절 처리
//     */
//    @PostMapping("/reservation/manager/{reservationId}")
//    public ResponseEntity<?> responseReservation(@PathVariable Long reservationId){
//
//        return ResponseEntity.ok(null);
//    }
//
//    /**
//     * 내 상점 정보 입력
//     */
//    @PostMapping("/store/register")
//    public ResponseEntity<?> registerStore(){
//
//        return ResponseEntity.ok(null);
//    }


}
