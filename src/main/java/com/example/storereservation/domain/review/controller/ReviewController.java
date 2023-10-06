package com.example.storereservation.domain.review.controller;

import com.example.storereservation.domain.review.dto.AddReview;
import com.example.storereservation.domain.review.dto.EditReview;
import com.example.storereservation.domain.review.dto.ReviewDto;
import com.example.storereservation.domain.review.service.ReviewService;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 쓰기
     */
    @ApiOperation("리뷰 쓰기")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/review/add/{reservationId}")
    public ResponseEntity<?> addReview(@PathVariable Long reservationId,
                                       @RequestBody AddReview.Request request,
                                       @AuthenticationPrincipal UserEntity user){
        ReviewDto reviewDto = reviewService.addReview(reservationId, user.getUserId(), request);

        return ResponseEntity.ok(AddReview.Response.fromDto(reviewDto));
    }

    /**
     * 내가 쓴 리뷰 리스트 확인
     */
    @ApiOperation(value = "리뷰 리스트 확인 - 유저")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/review/list/{userId}")
    public ResponseEntity<?> reviewList(@PathVariable String userId,
                                        @RequestParam(value = "p", defaultValue = "1") Integer page,
                                        @AuthenticationPrincipal UserEntity user){
        if(!user.getUserId().equals(userId)){
            throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
        }
        Page<ReviewDto> list = reviewService.reviewListByUserId(userId, page - 1);

        return ResponseEntity.ok(list);
    }

    /**
     * 리뷰 수정
     */
    @ApiOperation(value = "리뷰 수정")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/review/edit/{reviewId}")
    public ResponseEntity<?> editReview(@PathVariable Long reviewId,
                                        @RequestBody EditReview.Request request,
                                        @AuthenticationPrincipal UserEntity user){
        ReviewDto editedReview = reviewService.editReview(reviewId, user.getUserId(), request);

        return ResponseEntity.ok(EditReview.Response.fromDto(editedReview));
    }

}
