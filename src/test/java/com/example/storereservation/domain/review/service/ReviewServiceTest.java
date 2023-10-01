package com.example.storereservation.domain.review.service;

import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.service.ReservationService;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.domain.review.dto.AddReview;
import com.example.storereservation.domain.review.dto.EditReview;
import com.example.storereservation.domain.review.dto.ReviewDto;
import com.example.storereservation.domain.review.persist.ReviewEntity;
import com.example.storereservation.domain.review.persist.ReviewRepository;
import com.example.storereservation.domain.store.persist.StoreRepository;
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
import java.time.LocalTime;

import static com.example.storereservation.testsetting.TestConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰 쓰기_정상")
    void addReview() {
        //예약
        MakeReservation.Request req = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(req);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);

        Long id = reservation.getId();

        //given
        AddReview.Request request = AddReview.Request.builder()
                .rating(4)
                .text("hello")
                .build();
        //when
        ReviewDto addedReview = reviewService.addReview(id, TEST_USER_ID, request);

        //then
        assertThat(addedReview.getRating()).isEqualTo(request.getRating());
        assertThat(addedReview.getText()).isEqualTo(request.getText());
    }

    @Test
    @DisplayName("!!!리뷰 쓰기_USER_NOT_FOUND")
    void addReview_USER_NOT_FOUND() {
        //예약
        MakeReservation.Request req = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(req);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);

        Long id = reservation.getId();

        //given
        AddReview.Request request = AddReview.Request.builder()
                .rating(4)
                .text("hello")
                .build();
        //when
        //then
        try{
            reviewService.addReview(id, "!!!!!", request); //userId변경
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("!!!리뷰 쓰기_리뷰가 이미 존재하는 예약 건인 경우")
    void addReview_REVIEW_ALREADY_EXIST() {
        //예약
        MakeReservation.Request req = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(req);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);

        Long id = reservation.getId();

        //given
        AddReview.Request request = AddReview.Request.builder()
                .rating(4)
                .text("hello")
                .build();
        //when
        reviewService.addReview(id, TEST_USER_ID, request);
        //then
        try{
            reviewService.addReview(id, TEST_USER_ID, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.REVIEW_ALREADY_EXIST);
        }
    }

    @Test
    @DisplayName("!!!리뷰 쓰기_예약 상태가 USE_COMPLETE가 아닌 경우")
    void addReview_REVIEW_NOT_AVAILABLE() {
        //예약
        MakeReservation.Request req = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(req);
        //reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);
        //예약 상태 변경 x
        Long id = reservation.getId();

        //given
        AddReview.Request request = AddReview.Request.builder()
                .rating(4)
                .text("hello")
                .build();
        //when
        //then
        try{
            reviewService.addReview(id, TEST_USER_ID, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.REVIEW_NOT_AVAILABLE);
        }
    }

    @Test
    @DisplayName("!!!리뷰 쓰기_리뷰 별점이 0~5가 아닌 경우")
    void addReview_REVIEW_RATING_RANGE_ERROR() {
        //예약
        MakeReservation.Request req = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(req);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);

        Long id = reservation.getId();

        //given
        AddReview.Request request = AddReview.Request.builder()
                .rating(6)
                .text("hello")
                .build();
        //when
        //then
        try{
            reviewService.addReview(id, TEST_USER_ID, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.REVIEW_RATING_RANGE_ERROR);
        }
    }

    @Test
    @DisplayName("!!!리뷰 쓰기_리뷰 텍스트가 200자를 초과하는 경우")
    void addReview_REVIEW_TEXT_TOO_LONG() {
        //예약
        MakeReservation.Request req = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(req);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);

        Long id = reservation.getId();

        String str = "1234567890"; // 10글자
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 10; i++){
            sb.append(str);
        }
        sb.append("1");
        //given
        AddReview.Request request = AddReview.Request.builder()
                .rating(4)
                .text(sb.toString())
                .build();
        //when
        //then
        try{
            reviewService.addReview(id, TEST_USER_ID, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.REVIEW_TEXT_TOO_LONG);
        }
    }

    @Test
    @DisplayName("리뷰 리스트 조회 by userId")
    void reviewList() {
        //given
        String userId = TEST_USER_ID;

        //when
        Page<ReviewDto> list = reviewService.reviewListByUserId(userId, 0);

        //then
        for (ReviewDto reviewDto : list) {
            assertThat(reviewDto.getUserId()).isEqualTo(userId);
        }
    }

    @Test
    @DisplayName("리뷰 수정_정상")
    void editReview() {
        //예약
        MakeReservation.Request reservationReq = MakeReservation.Request.builder()
                .userId(TEST_USER_ID)
                .storeName(TEST_STORE_NAME)
                .people(4)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        ReservationDto reservation = reservationService.makeReservation(reservationReq);
        reservationService.changeReservationStatus(TEST_PARTNER_ID, reservation.getId(), ReservationStatus.USE_COMPLETE);
        //리뷰
        AddReview.Request reviewReq = AddReview.Request.builder()
                .rating(4)
                .text("hello")
                .build();

        Long reservationId = reservation.getId();
        String userId = reservation.getUserId();

        ReviewDto addedReview = reviewService.addReview(reservationId, userId, reviewReq);

        Long reviewId = addedReview.getId();

        //given
        EditReview.Request editReviewReq = EditReview.Request.builder()
                .rating(0)
                .text("edit").build();

        //when
        reviewService.editReview(addedReview.getId(), userId, editReviewReq);
        ReviewEntity editedReview = reviewRepository.findById(reviewId).get();

        //then
        assertThat(editedReview.getText()).isEqualTo("edit");
        assertThat(editedReview.getRating()).isEqualTo(0);
    }

    @Test
    @DisplayName("reviewListByStoreName => POSTMAN으로 테스트")
    void reviewListByStoreName() {
        //given

        //when

        //then

    }
}