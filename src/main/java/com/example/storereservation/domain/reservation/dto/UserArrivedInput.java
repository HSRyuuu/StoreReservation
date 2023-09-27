package com.example.storereservation.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserArrivedInput {
    private Long reservationId;
    private String phoneNumberLast4;
}
