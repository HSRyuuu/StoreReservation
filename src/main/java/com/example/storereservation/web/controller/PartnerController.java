package com.example.storereservation.web.controller;

import com.example.storereservation.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;


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
