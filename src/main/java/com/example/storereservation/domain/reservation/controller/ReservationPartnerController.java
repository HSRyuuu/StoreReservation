package com.example.storereservation.domain.reservation.controller;

import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.reservation.dto.ChangeReservationInput;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationPartnerController {
    private final ReservationService reservationService;


    /**
     * 파트너 - 예약 내역 모두 보기
     * @param status 예약 진행 상태 ReservationStatus (required = false)
     * @param date 예약 날짜 LocalDate (required = false)
     * @param page 페이지 (default = 1)
     * @param partner 로그인 된 파트너
     */
    @ApiOperation(value = "예약 내역 모두 보기", notes = "로그인 되어있는 파트너의 상점의 예약 내역 조회. 예약 상태, 날짜 별로 조회할 수 있다. \n" +
            "STATUS : REQUESTING, REFUSED, CONFIRM, ARRIVED, USE_COMPLETE, NO_SHOW")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @GetMapping("/partner/reservation/list")
    public ResponseEntity<?> reservationListForPartner(@RequestParam(required = false) String status,
                                                               @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date,
                                                               @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                               @AuthenticationPrincipal PartnerEntity partner){
        Page<ReservationDto> reservationList;

        if(Objects.isNull(status) && Objects.isNull(date)){
            reservationList = reservationService.listForPartner(partner.getPartnerId(), page - 1);
        }else if(Objects.nonNull(status) && Objects.isNull(date)){
            reservationList = reservationService.listForPartnerByStatus(
                    partner.getPartnerId(), ReservationStatus.of(status), page - 1);
        }else if(Objects.nonNull(date) && Objects.isNull(status)){
            reservationList = reservationService.listForPartnerByDate(
                    partner.getPartnerId(), date, page - 1);
        }else{
            reservationList = reservationService.listForPartnerByStatusAndDate(
                    partner.getPartnerId(), ReservationStatus.of(status), date, page - 1);
        }

        return ResponseEntity.ok(reservationList);
    }


    /**
     * 파트너 - 예약 상태 변경
     * @param id : reservationId
     * @param input : 변경하고자하는 상태
     * @param partner : 로그인 된 파트너
     */
    @ApiOperation(value = "예약 상태 변경", notes = "status : REQUESTING, REFUSED, CONFIRM, ARRIVED, USE_COMPLETE, NO_SHOW")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @PutMapping("/partner/reservation/{reservationId}")
    public ResponseEntity<?> changeReservationStatus(@PathVariable("reservationId") Long id,
                                                     @RequestBody ChangeReservationInput input,
                                                     @AuthenticationPrincipal PartnerEntity partner){
        reservationService.changeReservationStatus(partner.getPartnerId(), id, ReservationStatus.of(input.getStatus()));

        return ResponseEntity.ok(reservationService.reservationDetail(id, partner.getPartnerId()));
    }

}

