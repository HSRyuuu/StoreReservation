package com.example.storereservation.domain.reservation.controller;

import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
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
     * @param reservationId
     * @param userDetails : 로그인 정보
     * @return
     */
    @GetMapping("/reservation/detail/{reservationId}")
    public ResponseEntity<?> reservationDetail(@PathVariable() Long reservationId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        ReservationDto reservationDto =
                reservationService.reservationDetail(reservationId, userDetails.getUsername());

        return ResponseEntity.ok(reservationDto);
    }


    /**
     * 예약 내역 모두 보기 (user)
     */
    @GetMapping("/user/reservation/list")
    public ResponseEntity<?> reservationListForUser(@AuthenticationPrincipal UserEntity user){

        return ResponseEntity.ok(null);
    }

//    /**
//     * 예약 내역 모두 보기 (manager)
//     */
//    @GetMapping("/manager/reservation/list")
//    public ResponseEntity<?> reservationListForManager(){
//
//        return ResponseEntity.ok(null);
//    }


}
