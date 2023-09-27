package com.example.storereservation.domain.reservation.controller;

import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationUserController {
    private final ReservationService reservationService;

    /**
     * 예약 요청
     */
    @PostMapping("/reservation/request")
    public ResponseEntity<?> reservation(@RequestBody MakeReservation.Request request,
                                         @AuthenticationPrincipal UserEntity user){
        if(ObjectUtils.isEmpty(user)){
            throw new MyException(ErrorCode.LOGIN_REQUIRED);
        }
        request.setUserId(user.getUserId());
        ReservationDto reservationDto = reservationService.makeReservation(request);

        return ResponseEntity.ok(MakeReservation.Response.fromDto(reservationDto));
    }

    /**
     * 유저 - 예약 내역 모두 보기
     * @param user : 로그인 된 유저
     */
    @GetMapping("/reservation/list")
    public ResponseEntity<?> reservationListForUser(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                                    @AuthenticationPrincipal UserEntity user){
        Page<ReservationDto> reservationList = reservationService.listForUser(user.getUserId(), page - 1);
        return ResponseEntity.ok(reservationList);
    }

    /**
     * 유저 - 예약 내역 모두 보기 (status별)
     * @param status : 예약 진행 상태 ReservationStatus(enum)
     * @param user : 로그인 된 유저
     */
    @GetMapping("/reservation/list/{status}")
    public ResponseEntity<?> reservationListForUserByStatus(@PathVariable String status,
                                                            @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                            @AuthenticationPrincipal UserEntity user){
        Page<ReservationDto> reservationList =
                reservationService.listForUserByStatus(user.getUserId(), page - 1, ReservationStatus.of(status));

        return ResponseEntity.ok(reservationList);
    }

}

