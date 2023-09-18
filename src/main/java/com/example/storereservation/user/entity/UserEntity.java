package com.example.storereservation.user.entity;

import com.example.storereservation.web.security.MemberType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;

    private String userName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private MemberType memberType; //ROLE_USER

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
