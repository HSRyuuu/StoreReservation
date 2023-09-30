package com.example.storereservation.domain.review.controller;

import com.example.storereservation.domain.review.dto.AddReview;
import com.example.storereservation.domain.review.dto.ReviewDto;
import com.example.storereservation.domain.review.service.ReviewService;
import com.example.storereservation.domain.store.service.StoreService;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final StoreService storeService;

    /**
     * 리뷰 쓰기
     */
    @PostMapping("/review/add/{reservationId}")
    public ResponseEntity<?> addReview(@PathVariable Long reservationId,
                                       @RequestBody AddReview.Request request,
                                       @AuthenticationPrincipal UserDetails user){

        ReviewDto reviewDto = reviewService.addReview(reservationId, user.getUsername(), request);

        storeService.updateRating(reviewDto);//매장 리뷰 업데이트

        return ResponseEntity.ok(AddReview.Response.fromDto(reviewDto));
    }
    /**
     * 내가 쓴 리뷰 리스트 확인
     */
    @GetMapping("/review/list/{userId}")
    public ResponseEntity<?> reviewList(@PathVariable String userId,
                                        @RequestParam(value = "p", defaultValue = "1") Integer page,
                                        @AuthenticationPrincipal UserEntity user){
        if(!user.getUserId().equals(userId)){
            throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
        }
        Page<ReviewDto> list = reviewService.reviewList(userId, page - 1);

        return ResponseEntity.ok(list);
    }

    /**
     * TODO 리뷰 수정
     */
    @PutMapping("/review/edit/{reviewId}")
    public ResponseEntity<?> editReview(@PathVariable Long reviewId){

        return ResponseEntity.ok(null);
    }

}
