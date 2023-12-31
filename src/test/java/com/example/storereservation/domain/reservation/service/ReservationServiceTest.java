package com.example.storereservation.domain.reservation.service;

import com.example.storereservation.domain.partner.service.PartnerService;
import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
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
    @DisplayName("!!!예약_USER_NOT_FOUND")
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
    @DisplayName("!!!예약_STORE_NOT_FOUND")
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
    @DisplayName("파트너 예약 내역 확인_정상")
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
    @DisplayName("파트너 예약 리스트 상태 별 검색_정상")
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

        Page<ReservationDto> list = reservationService.listForPartnerByStatus(TEST_PARTNER_ID, ReservationStatus.T_E_S_T, 0);

        int cnt = 0;
        //then
        for (ReservationDto r : list) {
            assertThat(r.getStatus()).isEqualTo(ReservationStatus.T_E_S_T);
            cnt++;
        }
        assertThat(cnt).isEqualTo(2);

    }

    @Test
    @DisplayName("파트너 예약 날짜 별 검색_정상")
    void listForPartnerByDate() {
        //given
        MakeReservation.Request request1 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.of(9999,1,1))
                .time(LocalTime.now())
                .build();
        reservationService.makeReservation(request1);

        MakeReservation.Request request2 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.of(9999,1,1))
                .time(LocalTime.now())
                .build();
        reservationService.makeReservation(request2);

        MakeReservation.Request request3 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.of(9999,1,1))
                .time(LocalTime.now())
                .build();
        reservationService.makeReservation(request3);

        //when
        Page<ReservationDto> list = reservationService.listForPartnerByDate(TEST_PARTNER_ID, LocalDate.of(9999,1,1), 0);

        int cnt = 0;
        //then
        for (ReservationDto r : list) {
            assertThat(r.getTime().getMonthValue()).isEqualTo(1);
            assertThat(r.getTime().getDayOfMonth()).isEqualTo(1);
            cnt++;
        }
        assertThat(cnt).isEqualTo(3);
    }

    @Test
    @DisplayName("파트너 예약 검색 상태 + 날짜 _정상")
    void listForPartnerByStatusAndDate() {
        //given
        MakeReservation.Request request1 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.of(9999,1,1))
                .time(LocalTime.now())
                .build();
        ReservationDto r1 = reservationService.makeReservation(request1);

        MakeReservation.Request request2 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.of(9999,1,1))
                .time(LocalTime.now())
                .build();
        ReservationDto r2 = reservationService.makeReservation(request2);

        MakeReservation.Request request3 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.of(9999,1,1))
                .time(LocalTime.now())
                .build();
        ReservationDto r3 = reservationService.makeReservation(request3);

        //when
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r2.getId(), ReservationStatus.T_E_S_T);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r3.getId(), ReservationStatus.T_E_S_T);

        Page<ReservationDto> list = reservationService.listForPartnerByStatusAndDate(TEST_PARTNER_ID, ReservationStatus.T_E_S_T, LocalDate.of(9999,1,1), 0);

        int cnt = 0;
        //then
        for (ReservationDto r : list) {
            assertThat(r.getTime().getMonthValue()).isEqualTo(1);
            assertThat(r.getTime().getDayOfMonth()).isEqualTo(1);
            cnt++;
        }
        assertThat(cnt).isEqualTo(2);
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
    @DisplayName("유저-예약 내역 확인")
    void listForUser() {
        //given
        String userId = TEST_USER_ID;
        Integer page = 0; // 컨트롤러에서 -1 해줘서 service에서는 0부터 시작
        //when
        Page<ReservationDto> list = reservationService.listForUser(userId, page);

        //then
        for (ReservationDto reservationDto : list) {
            assertThat(reservationDto.getUserId()).isEqualTo(userId);
        }
    }
    @Test
    @DisplayName("!!!유저-예약 내역 확인")
    void listForUser_RESERVATION_IS_ZERO() {
        //given
        String userId = "asdfasdf";
        Integer page = 0; // 컨트롤러에서 -1 해줘서 service에서는 0부터 시작
        //when
        //then
        try{
            reservationService.listForUser(userId, page);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_IS_ZERO);
        }
    }

    @Test
    @DisplayName("유저 예약 리스트(상태 별)_정상")
    void listForUserByStatus() {
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

        reservationService.changeReservationStatus(TEST_PARTNER_ID, r3.getId(), ReservationStatus.T_E_S_T);

        Page<ReservationDto> list = reservationService.listForUserByStatus(TEST_PARTNER_ID, 0, ReservationStatus.T_E_S_T);

        //then
        for (ReservationDto r : list) {
            assertThat(r.getStatus()).isEqualTo(ReservationStatus.T_E_S_T);
        }
    }
    @Test
    @DisplayName("!!!유저 예약 리스트(상태별)_검색 결과 없음")
    void listForUserByStatus_RESERVATION_IS_ZERO() {
        //given
        MakeReservation.Request request1 = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto r = reservationService.makeReservation(request1);

        //when
        //then
        try{
            reservationService.listForUserByStatus(TEST_USER_ID, 0, ReservationStatus.T_E_S_T);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_IS_ZERO);
        }
    }

    @Test
    @DisplayName("매장 도착 확인_정상")
    void arrivedCheck() {
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.now())
                .build();
        ReservationDto r = reservationService.makeReservation(request);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r.getId(), ReservationStatus.CONFIRM);
        //given
        Long reservationId = r.getId();
        String phoneNumberLast4 = r.getPhone().substring(7);

        //when
        ReservationDto result = reservationService.arrivedCheck(reservationId, phoneNumberLast4);

        //then
        assertThat(TEST_USER_ID).isEqualTo(result.getUserId());
    }
    @Test
    @DisplayName("!!!매장 도착 확인_RESERVATION_NOT_FOUND")
    void arrivedCheck_RESERVATION_NOT_FOUND() {
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.now())
                .build();
        ReservationDto r = reservationService.makeReservation(request);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r.getId(), ReservationStatus.CONFIRM);
        //given
        Long reservationId = -123L;
        String phoneNumberLast4 = r.getPhone().substring(7);

        //when
        //then
        try {
            reservationService.arrivedCheck(reservationId, phoneNumberLast4);
        }catch (MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }
    @Test
    @DisplayName("!!!매장 도착 확인_전화번호 틀림")
    void arrivedCheck_RESERVATION_PHONE_NUMBER_INCORRECT() {
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.now())
                .build();
        ReservationDto r = reservationService.makeReservation(request);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r.getId(), ReservationStatus.CONFIRM);
        //given
        Long reservationId = r.getId();
        String phoneNumberLast4 = "wrong";

        //when
        //then
        try {
            reservationService.arrivedCheck(reservationId, phoneNumberLast4);
        }catch (MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_PHONE_NUMBER_INCORRECT);
        }
    }
    @Test
    @DisplayName("!!!매장 도착 확인_예약 상태 != CONFIRM")
    void arrivedCheck_RESERVATION_STATUS_CHECK_ERROR() {
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.now())
                .build();
        ReservationDto r = reservationService.makeReservation(request);
        //Status 변경 하지 않음.
        //reservationService.changeReservationStatus(TEST_PARTNER_ID, r.getId(), ReservationStatus.CONFIRM);

        //given
        Long reservationId = r.getId();
        String phoneNumberLast4 = r.getPhone().substring(7);

        //when
        //then
        try {
            reservationService.arrivedCheck(reservationId, phoneNumberLast4);
        }catch (MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_STATUS_CHECK_ERROR);
        }
    }
    @Test
    @DisplayName("!!!매장 도착 확인_시간 문제")
    void arrivedCheck_RESERVATION_TIME_CHECK_ERROR() {
        MakeReservation.Request request = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto r = reservationService.makeReservation(request);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, r.getId(), ReservationStatus.CONFIRM);

        //given
        Long reservationId = r.getId();
        String phoneNumberLast4 = r.getPhone().substring(7);

        //when
        //then
        try {
            reservationService.arrivedCheck(reservationId, phoneNumberLast4);
        }catch (MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_TIME_CHECK_ERROR);
        }
    }
}