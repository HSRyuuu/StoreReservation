package com.example.storereservation.domain.review.persist;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "REVIEW")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;
    private String userId;
    private String storeName;
    private double rating;
    private String text;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
