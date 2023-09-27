package com.example.storereservation.domain.reservation.controller;

import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.reservation.dto.ChangeReservationInput;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationPartnerController {
    private final ReservationService reservationService;

    /**
     * 파트너 - 예약 내역 모두 보기
     */
    @GetMapping("/partner/reservation/list")
    public ResponseEntity<?> reservationListForPartner(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                                       @AuthenticationPrincipal PartnerEntity partner){
        Page<ReservationDto> reservationList = reservationService.listForPartner(partner.getPartnerId(), page - 1);

        return ResponseEntity.ok(reservationList);
    }

    /**
     * 파트너 - 예약 내역 모두 보기 (status별)
     * @param status 예약 진행 상태 ReservationStatus(enum)
     * @param partner : 로그인된 파트너
     *                TODO : 날짜 별 보기 추가하기
     */
    @GetMapping("/partner/reservation/list/{status}")
    public ResponseEntity<?> reservationListForPartnerByStatus(@PathVariable String status,
                                                               @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                               @AuthenticationPrincipal PartnerEntity partner){
        Page<ReservationDto> reservationList = reservationService.listForPartnerByStatus(
                partner.getPartnerId(), page - 1, ReservationStatus.of(status));

        return ResponseEntity.ok(reservationList);
    }

    /**
     * 파트너 - 예약 상태 변경
     * @param id : reservationId
     * @param input : 변경하고자하는 상태
     * @param partner : 로그인 된 파트너
     */
    @PutMapping("/partner/reservation/{reservationId}")
    public ResponseEntity<?> changeReservationStatus(@PathVariable("reservationId") Long id,
                                                     @RequestBody ChangeReservationInput input,
                                                     @AuthenticationPrincipal PartnerEntity partner){
        reservationService.changeReservationStatus(partner.getPartnerId(), id, ReservationStatus.of(input.getStatus()));

        return ResponseEntity.ok(reservationService.reservationDetail(id, partner.getPartnerId()));
    }

}

