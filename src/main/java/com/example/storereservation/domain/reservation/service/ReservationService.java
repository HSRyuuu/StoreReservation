package com.example.storereservation.domain.reservation.service;

import com.example.storereservation.domain.reservation.dto.MakeReservation;
import com.example.storereservation.domain.reservation.dto.ReservationDto;
import com.example.storereservation.domain.reservation.persist.ReservationEntity;
import com.example.storereservation.domain.reservation.persist.ReservationRepository;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.example.storereservation.domain.store.persist.StoreEntity;
import com.example.storereservation.domain.store.persist.StoreRepository;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.domain.user.persist.UserRepository;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public ReservationDto makeReservation(MakeReservation.Request request){
        ReservationEntity reservation = makeReservationEntity(request);
        ReservationEntity saved = reservationRepository.save(reservation);
        log.info("reservation id : {}", saved.getId());
        return ReservationDto.fromEntity(reservation);
    }
    private ReservationEntity makeReservationEntity(MakeReservation.Request request){
        UserEntity user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));
        StoreEntity store = storeRepository.findByStoreName(request.getStoreName())
                .orElseThrow(() -> new MyException(ErrorCode.STORE_NOT_FOUND));
        LocalDateTime reservationTime = LocalDateTime.of(request.getDate(), request.getTime());

        return ReservationEntity.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .partnerId(store.getPartnerId())
                .storeName(store.getStoreName())
                .people(request.getPeople())
                .status(ReservationStatus.REQUESTING)
                .time(reservationTime)
                .build();

    }
}
