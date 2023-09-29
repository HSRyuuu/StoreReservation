package com.example.storereservation.domain.review.controller;

import com.example.storereservation.domain.review.dto.AddReview;
import com.example.storereservation.domain.review.dto.ReviewDto;
import com.example.storereservation.domain.review.service.ReviewService;
import com.example.storereservation.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
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
     * TODO 내가 쓴 리뷰 리스트 확인
     *
     */
    @GetMapping("/review/list/{userId}")
    public ResponseEntity<?> reviewList(@PathVariable String userId){

        return ResponseEntity.ok(null);
    }

    /**
     * TODO 리뷰 수정
     */
    @PutMapping("/review/edit/{reviewId}")
    public ResponseEntity<?> editReview(@PathVariable Long reviewId){

        return ResponseEntity.ok(null);
    }

}
