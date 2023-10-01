package com.example.storereservation.domain.reservation.controller;

import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 상세정보 보기
     * - @AuthenticationPricipal로 로그인 된 유저 정보를 받아서 유저 or 파트너에게 정보 주기
     * @param userDetails : 로그인 정보
     */
    @GetMapping("/reservation/detail/{reservationId}")
    public ResponseEntity<?> reservationDetail(@PathVariable Long reservationId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        ReservationDto reservationDto =
                reservationService.reservationDetail(reservationId, userDetails.getUsername());
        return ResponseEntity.ok(reservationDto);
    }

}
