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
import com.example.storereservation.global.type.PageConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * 매장 예약
     * @param request : userId, storeName, people(인원 수)
     * @return
     */
    public ReservationDto makeReservation(MakeReservation.Request request){
        ReservationEntity reservation = makeReservationEntity(request);
        ReservationEntity saved = reservationRepository.save(reservation);
        log.info("reservation id : {}", saved.getId());
        return ReservationDto.fromEntity(reservation);
    }

    /**
     * 매장 예약 Request를 바탕으로 ReservationEntity 생성
     */
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
                .statusUpdatedAt(LocalDateTime.now())
                .time(reservationTime)
                .build();
    }

    /**
     * 예약 상세 정보
     */
    public ReservationDto reservationDetail(Long id, String username){
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new MyException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!this.validateReservationAccessAuthority(username, reservation)) {
            throw new MyException(ErrorCode.ACCESS_DENIED);
        }

        return ReservationDto.fromEntity(reservation);
    }

    /**
     * userDetails의 username이 reservationDto의 userId 또는 partnerId와 일치하는지 확인
     */
    private boolean validateReservationAccessAuthority(String username, ReservationEntity reservation) {
        if (reservation.getUserId().equals(username)) {
            log.info("UserID : {}, 예약 내역 확인", username);
            return true;
        } else if (reservation.getPartnerId().equals(username)) {
            log.info("PartnerId : {}, 예약 내역 확인", username);
            return true;
        }
        return false;
    }

    /**
     * partner ID로 예약 내역 확인
     * @param partnerId
     * @param page
     * sort : 시간 빠른 순
     * @return
     */
    public Page<ReservationDto> getListForPartner(String partnerId, Integer page){
        PageRequest pageRequest = PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE);
        Page<ReservationEntity> reservations =
                reservationRepository.findByPartnerIdOrderByTime(partnerId, pageRequest);
        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }



}
