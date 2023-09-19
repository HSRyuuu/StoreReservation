package com.example.storereservation.domain.reservation.type;

public enum ReservationStatus {
    /**
     * 예약 요청 중
     */
    REQUESTING,
    /**
     * 예약 거절 상태
     */
    REFUSED,
    /**
     * 예약 승인 상태
     */
    CONFIRM,
    /**
     * 도착 확인
     */
    ARRIVED,
    /**
     * 이용 완료
     */
    USE_COMPLETE,
    /**
     * 예약 승인 후 이용하지 않음(no-show)
     */
    NO_SHOW
}
