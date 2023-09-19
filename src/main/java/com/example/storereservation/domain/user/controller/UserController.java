package com.example.storereservation.domain.user.controller;

import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final ReservationService reservationService;

    /**
     * 예약 요청
     */
    @PostMapping("/reservation/request")
    public ResponseEntity<?> reservation(@RequestBody MakeReservation.Request request,
                                         @AuthenticationPrincipal UserEntity user){
        if(!user.getUserId().equals(request.getUserId())){
            throw new MyException(ErrorCode.INPUT_USER_ID_NOT_MATCH);
        }
        ReservationDto reservationDto = reservationService.makeReservation(request);

        return ResponseEntity.ok(MakeReservation.Response.fromDto(reservationDto));
    }


}
