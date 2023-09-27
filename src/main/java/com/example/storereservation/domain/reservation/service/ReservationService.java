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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * 유저 - 매장 예약
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
     * 유저/파트너 - 예약 상세 정보
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
     * 파트너 - partner ID로 예약 내역 확인
     * @param partnerId
     * @param page
     * sort : 최신순
     */
    public Page<ReservationDto> listForPartner(String partnerId, Integer page){
        Page<ReservationEntity> reservations =
                reservationRepository.findByPartnerIdOrderByTimeDesc(
                        partnerId,
                        PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE)
                );

        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }

    /**
     * 파트너 - partner ID와 ReservationStatus로 내역 확인
     * sort : 시간 빠른 순
     */
    public Page<ReservationDto> listForPartnerByStatus(String partnerId, ReservationStatus status, Integer page){

        Page<ReservationEntity> reservations =
                reservationRepository.findByPartnerIdAndStatusOrderByTime(
                        partnerId,
                        status,
                        PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE)
                );

        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }

    /**
     * 파트너 - partner ID와 예약 날짜로 내역 확인
     * sort : 시간 빠른 순
     */
    public Page<ReservationDto> listForPartnerByDate(String partnerId, LocalDate date, Integer page){

        Page<ReservationEntity> reservations =
                reservationRepository.findByPartnerIdAndTimeBetweenOrderByTime(
                        partnerId,
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX),
                        PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE)
                );

        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }

    /**
     * 파트너 - partner ID와 예약 상태(status), 예약 날짜(time)로 내역 확인
     * sort : 시간 빠른 순
     */
    public Page<ReservationDto> listForPartnerByStatusAndDate(String partnerId,ReservationStatus status, LocalDate date, Integer page){

        Page<ReservationEntity> reservations =
                reservationRepository.findByPartnerIdAndStatusAndTimeBetweenOrderByTime(
                        partnerId,
                        status,
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX),
                        PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE)
                );

        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }


    /**
     * 파트너 - 예약 상태 변경
     * @param reservationId
     * @param status
     * @param partnerId
     */
    public void changeReservationStatus(String partnerId, Long reservationId, ReservationStatus status){
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new MyException(ErrorCode.RESERVATION_NOT_FOUND));

        if(!reservation.getPartnerId().equals(partnerId)){
            throw new MyException(ErrorCode.RESERVATION_UPDATE_AUTH_FAIL);
        }
        reservation.setStatus(status);
        reservationRepository.save(reservation);
    }



    /**
     * 유저 - user ID로 예약 내역 확인
     * sort : 시간 빠른 순
     */
    public Page<ReservationDto> listForUser(String userId, Integer page){
        Page<ReservationEntity> reservations =
                reservationRepository.findByUserIdOrderByTimeDesc(
                        userId,
                        PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE)
                );

        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }

    /**
     * 유저 - userId와 ReservationStatus로 내역 확인
     * sort : 시간 빠른 순
     */
    public Page<ReservationDto> listForUserByStatus(String userId, Integer page, ReservationStatus status){

        Page<ReservationEntity> reservations =
                reservationRepository.findByUserIdAndStatusOrderByTime(
                        userId,
                        status,
                        PageRequest.of(page, PageConst.RESERVATION_LIST_PAGE_SIZE)
                );

        if(reservations.getSize() == 0){
            throw new MyException(ErrorCode.RESERVATION_IS_ZERO);
        }
        return reservations.map(reservation -> ReservationDto.fromEntity(reservation));
    }

    /**
     * 도착 확인
     * @param reservationId
     * @return boolean result
     */
    public ReservationDto arrivedCheck(Long reservationId, String inputPhoneNumberLast4){
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new MyException(ErrorCode.RESERVATION_NOT_FOUND));
        validate(reservation, inputPhoneNumberLast4);
        reservation.setStatus(ReservationStatus.ARRIVED);
        reservationRepository.save(reservation);
        return ReservationDto.fromEntity(reservation);
    }

    /**
     * 도착 확인 validation
     * 1. 전화번호 뒷 4자리 확인
     * 2. 예약 상태가 CONFIRM인지 확인
     * 3. 예약 상태에 맞게 왔는지 확인
     */
    private void validate(ReservationEntity reservation, String inputPhoneNumberLast4){
        String rightPhoneNumberLast4 = reservation.getPhone().substring(7);

        if(!rightPhoneNumberLast4.equals(inputPhoneNumberLast4)){
            throw new MyException(ErrorCode.RESERVATION_PHONE_NUMBER_INCORRECT);
        }else if(!reservation.getStatus().equals(ReservationStatus.CONFIRM)){
            throw new MyException(ErrorCode.RESERVATION_STATUS_CHECK_ERROR);
        }else if(LocalDateTime.now().isAfter(reservation.getTime().minusMinutes(10L))){
            throw new MyException(ErrorCode.RESERVATION_TIME_CHECK_ERROR);
            //(현재시간) > (예약 시간 - 10분) => 10분 전에 도착하지 못함.
        }
    }

}
