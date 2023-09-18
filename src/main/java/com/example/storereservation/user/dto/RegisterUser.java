package com.example.storereservation.user.dto;


import com.example.storereservation.web.security.MemberType;
import com.example.storereservation.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class RegisterUser {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        //입력받은 값
        private String userId;

        private String password;
        private String passwordCheck;

        private String userName;
        private String phone;

        private MemberType memberType;//ROLE_USER

        public static UserEntity toEntity(Request request){
            return UserEntity.builder()
                    .userId(request.getUserId())
                    .password(request.getPassword())
                    .userName(request.getUserName())
                    .phone(request.getPhone())
                    .memberType(MemberType.ROLE_USER)
                    .createAt(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        //입력받은 값
        private String userId;

        private String userName;
        private String phone;
        //서버 측 설정 값
        private LocalDateTime createAt;
        private MemberType memberType;

        public static Response fromDto(UserDto userDto){
            return Response.builder()
                    .userId(userDto.getUserId())
                    .userName(userDto.getUserName())
                    .phone(userDto.getPhone())
                    .memberType(userDto.getMemberType())
                    .createAt(userDto.getCreateAt())
                    .build();
        }
    }

}
