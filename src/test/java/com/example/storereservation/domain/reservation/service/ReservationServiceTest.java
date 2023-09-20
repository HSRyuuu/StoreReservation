package com.example.storereservation.domain.reservation.service;

import com.example.storereservation.domain.partner.dto.AddStore;
import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.service.PartnerService;
import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.storereservation.testsetting.TestConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    PartnerService partnerService;

    @Test
    @DisplayName("예약_성공")
    void makeReservation() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        //when
        ReservationDto reservation = reservationService.makeReservation(request);

        //then
        assertThat(reservation.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(reservation.getStoreName()).isEqualTo(TEST_STORE_NAME);
        assertThat(reservation.getPeople()).isEqualTo(4);
        assertThat(reservation.getTime().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth());
    }

    @Test
    @DisplayName("예약_USER_NOT_FOUND")
    void makeReservation_USER_NOT_FOUND() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId("asdfasdf")
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        //when
        try {
            reservationService.makeReservation(request);
        } catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("예약_STORE_NOT_FOUND")
    void makeReservation_STORE_NOT_FOUND() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName("asdfasadf")
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        //when
        try {
            reservationService.makeReservation(request);
        } catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("예약 상세_성공")
    void reservationDetail() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto saved = reservationService.makeReservation(request);
        Long id = saved.getId();

        //when
        ReservationDto find1 = reservationService.reservationDetail(id, TEST_USER_ID); //유저 로그인
        ReservationDto find2 = reservationService.reservationDetail(id, TEST_PARTNER_ID); //파트너 로그인

        //then
        assertThat(find1.getUserId()).isEqualTo(saved.getUserId());
        assertThat(find1.getStoreName()).isEqualTo(saved.getStoreName());
        assertThat(find1.getPeople()).isEqualTo(saved.getPeople());

        assertThat(find1.getUserId()).isEqualTo(saved.getUserId());
        assertThat(find1.getStoreName()).isEqualTo(saved.getStoreName());
        assertThat(find1.getPeople()).isEqualTo(saved.getPeople());

    }

    @Test
    @DisplayName("!!!예약 상세_예약이 존재하지 않음")
    void reservationDetail_RESERVATION_NOT_FOUND() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto saved = reservationService.makeReservation(request);
        Long id = saved.getId();

        //when
        //then
        try {
            reservationService.reservationDetail((long) Integer.MAX_VALUE, TEST_USER_ID);
        } catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("!!!예약 상세_예약 접근 권한 없음")
    void reservationDetail_ACCESS_DENIED() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto saved = reservationService.makeReservation(request);
        Long id = saved.getId();

        //when
        //then
        try {
            reservationService.reservationDetail(id, "asdfsadf");
        } catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ACCESS_DENIED);
        }
    }

    @Test
    @DisplayName("파트너_예약 내역 확인")
    void listForPartner() {
        //given
        String partnerId = TEST_PARTNER_ID;
        Integer page = 0; // 컨트롤러에서 -1 해줘서 service에서는 0부터 시작
        //when
        Page<ReservationDto> list = reservationService.listForPartner(partnerId, page);

        //then
        for (ReservationDto reservationDto : list) {
            assertThat(reservationDto.getPartnerId()).isEqualTo(partnerId);
        }
    }
    @Test
    @DisplayName("!!!파트너_예약 내역 확인_예약 내역 없음")
    void listForPartner_RESERVATION_NOT_FOUND() {
        //given
        String partnerId = TEST_PARTNER_ID;
        Integer page = Integer.MAX_VALUE; // 컨트롤러에서 -1 해줘서 service에서는 0부터 시작
        //when
        //then
        try{
            reservationService.listForPartner(partnerId, page);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("예약 상태 변경_정상")
    void changeReservationStatus() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservationDto = reservationService.makeReservation(request);
        Long id = reservationDto.getId();
        String partnerId = reservationDto.getPartnerId();

        //when
        reservationService.changeReservationStatus(partnerId, id, ReservationStatus.CONFIRM);

        ReservationDto find = reservationService.reservationDetail(id, partnerId);

        //then
        assertThat(find.getStatus()).isEqualTo(ReservationStatus.CONFIRM);

    }

    @Test
    @DisplayName("!!!예약 상태 변경_예약 내역 없음")
    void changeReservationStatus_RESERVATION_NOT_FOUND() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservationDto = reservationService.makeReservation(request);
        Long id = reservationDto.getId();
        String partnerId = reservationDto.getPartnerId();

        //when
        //then
        try{
            reservationService.changeReservationStatus(partnerId, (long)Integer.MIN_VALUE, ReservationStatus.CONFIRM);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("!!!예약 상태 변경_파트너 아이디 불일치")
    void changeReservationStatus_RESERVATION_UPDATE_AUTH_FAIL() {
        //given
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservationDto = reservationService.makeReservation(request);
        Long id = reservationDto.getId();
        String partnerId = reservationDto.getPartnerId();

        //when
        //then
        try{
            reservationService.changeReservationStatus("asdfasdf", id, ReservationStatus.CONFIRM);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_UPDATE_AUTH_FAIL);
        }
    }

    @Test
    @DisplayName("예약 상태 리스트_정상")
    void listForPartnerByStatus() {

        //given
        MakeReservation.Request request1 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto r1 = reservationService.makeReservation(request1);

        MakeReservation.Request request2 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto r2 = reservationService.makeReservation(request2);

        MakeReservation.Request request3 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto r3 = reservationService.makeReservation(request3);

        //when
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r2.getId(), ReservationStatus.T_E_S_T);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r3.getId(), ReservationStatus.T_E_S_T);

        Page<ReservationDto> list = reservationService.listForPartnerByStatus(TEST_PARTNER_ID, 0, ReservationStatus.T_E_S_T);

        //then
        for (ReservationDto r : list) {
            assertThat(r.getStatus()).isEqualTo(ReservationStatus.T_E_S_T);
        }

    }

    @Test
    @DisplayName("!!!예약 상태 리스트_RESERVATION_IS_ZERO")
    void listForPartnerByStatus_RESERVATION_IS_ZERO() {

        //given
        MakeReservation.Request request1 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto r1 = reservationService.makeReservation(request1);


        //when
        //then
        try{
            reservationService.listForPartnerByStatus(TEST_PARTNER_ID, 0, ReservationStatus.T_E_S_T);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_IS_ZERO);
        }


    }


}