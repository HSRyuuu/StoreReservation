package com.example.storereservation.domain.reservation.type;

import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import org.springframework.util.StringUtils;

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
    NO_SHOW,

    /**
     * 테스트용
     */
    T_E_S_T;

    public static ReservationStatus of(String status){
        status = status.toUpperCase();
        if(!StringUtils.hasText(status)){
            throw new MyException(ErrorCode.RESERVATION_STATUS_CODE_REQUIRED);
        }
        for(ReservationStatus rs : ReservationStatus.values()){
            if(rs.toString().equals(status)){
                return rs;
            }
        }

        throw new MyException(ErrorCode.RESERVATION_STATUS_CODE_ILLEGAL_ARGUMENT);

    }
}
