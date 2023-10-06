package com.example.storereservation.domain.reservation.controller;

import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.dto.UserArrivedComplete;
import com.example.storereservation.domain.reservation.dto.UserArrivedInput;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.domain.user.persist.UserEntity;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationUserController {
    private final ReservationService reservationService;

    /**
     * 예약 요청
     */
    @ApiOperation(value = "예약 요청")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/reservation/request")
    public ResponseEntity<?> reservation(@RequestBody MakeReservation.Request request,
                                         @AuthenticationPrincipal UserEntity user){
        request.setUserId(user.getUserId());
        ReservationDto reservationDto = reservationService.makeReservation(request);

        return ResponseEntity.ok(MakeReservation.Response.fromDto(reservationDto));
    }

    /**
     * 유저 - 예약 내역 모두 보기
     * 정렬 : 최신 순
     * @param user : 로그인 된 유저
     */
    @ApiOperation(value = "예약 내역 모두 보기")
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @ApiOperation(value = "예약 내역 모두 보기 (status 별 조회)", notes = "STATUS : REQUESTING, REFUSED, CONFIRM, ARRIVED, USE_COMPLETE, NO_SHOW")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/reservation/list/{status}")
    public ResponseEntity<?> reservationListForUserByStatus(@PathVariable ReservationStatus status,
                                                            @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                            @AuthenticationPrincipal UserEntity user){
        Page<ReservationDto> reservationList =
                reservationService.listForUserByStatus(user.getUserId(), page - 1, status);

        return ResponseEntity.ok(reservationList);
    }

    /**
     * 매장 도착 확인
     * @param input (reservationId, phoneNumberLast4)
     */
    @ApiOperation(value = "매장 도착 확인", notes = "매장에 도착해서 예약 ID와 전화 번호 뒷 4자리로 도착 확인을 할 수 있다. ")
    @PostMapping("/reservation/arrived")
    public ResponseEntity<?> arrivedHandling(@RequestBody UserArrivedInput input){
        ReservationDto reservationDto = reservationService.arrivedCheck(input.getReservationId(), input.getPhoneNumberLast4());

        return ResponseEntity.ok(new UserArrivedComplete(reservationDto));
    }



}

