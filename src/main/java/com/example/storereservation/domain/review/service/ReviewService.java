package com.example.storereservation.domain.review.service;

import com.example.storereservation.domain.reservation.persist.ReservationEntity;
import com.example.storereservation.domain.reservation.persist.ReservationRepository;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.domain.review.dto.AddReview;
import com.example.storereservation.domain.review.dto.ReviewDto;
import com.example.storereservation.domain.review.persist.ReviewEntity;
import com.example.storereservation.domain.review.persist.ReviewRepository;
import com.example.storereservation.domain.user.persist.UserRepository;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 쓰기
     * @param reservationId : 예약 id
     * @param userId : 유저 id
     * 해당 예약의 userId와 로그인 유저의 userId 일치 확인
     */
    public ReviewDto addReview(Long reservationId, String userId, AddReview.Request request){
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new MyException(ErrorCode.RESERVATION_NOT_FOUND));

        validateReviewAvailable(reservation, userId); //해당 리뷰를 쓸 권한이 있는지 검증
        validateReviewDetail(request);//리뷰의 별점 범위, 텍스트 길이 검증

        ReviewEntity review = AddReview.Request.toEntity(request, reservation);
        reviewRepository.save(review);

        return ReviewDto.fromEntity(review);
    }

    private void validateReviewAvailable(ReservationEntity reservation, String userId){
        //유저 로그인 상태가 아닌 경우
        if(!userRepository.existsByUserId(userId)){
            throw new MyException(ErrorCode.USER_NOT_FOUND);
        }
        // 해당 예약의 userID와 리뷰를 쓴 유저 ID가 일치하지 않는 경우
        if(!reservation.getUserId().equals(userId)){
            throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
        }
        // 해당 예약에 대한 리뷰가 이미 존재하는 경우
        if(reviewRepository.existsByReservationId(reservation.getId())){
            throw new MyException(ErrorCode.REVIEW_ALREADY_EXIST);
        }
        // 해당 예약이 USE_COMPLETE 상태가 아니라서 리뷰를 쓸 수 없는 경우
        if(!reservation.getStatus().equals(ReservationStatus.USE_COMPLETE)){
            throw new MyException(ErrorCode.REVIEW_NOT_AVAILABLE);
        }
    }

    private void validateReviewDetail(AddReview.Request request){
        if(request.getRating() > 5 || request.getRating() < 0){
            throw new MyException(ErrorCode.REVIEW_RATING_RANGE_ERROR);
        }
        if(request.getText().length() > 200){
            throw new MyException(ErrorCode.REVIEW_TEXT_TOO_LONG);
        }
    }


}
