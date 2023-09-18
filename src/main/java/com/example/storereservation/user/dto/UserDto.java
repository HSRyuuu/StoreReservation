package com.example.storereservation.user.dto;


import com.example.storereservation.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;
    private String password;

    private String name;
    private String phone;

    private String memberType; //ROLE_USER

    private LocalDateTime createAt;
    private LocalDateTime updateDt;

    public static UserDto fromEntity(UserEntity userEntity){
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .phone(userEntity.getPhone())
                .memberType(userEntity.getMemberType())
                .createAt(userEntity.getCreateAt())
                .updateDt(userEntity.getUpdateAt())
                .build();
    }
}
