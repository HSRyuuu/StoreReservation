package com.example.storereservation.domain.reservation.service;

import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.storereservation.testsetting.TestConst.TEST_STORE_NAME;
import static com.example.storereservation.testsetting.TestConst.TEST_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

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
        try{
            reservationService.makeReservation(request);
        }catch(MyException e){
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
        try{
            reservationService.makeReservation(request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
        }
    }
}