package com.example.storereservation.user.service;

import com.example.storereservation.exception.ErrorCode;
import com.example.storereservation.exception.MyException;
import com.example.storereservation.user.dto.RegisterUser;
import com.example.storereservation.user.dto.UserDto;
import com.example.storereservation.user.entity.UserEntity;
import com.example.storereservation.user.repository.UserRepository;
import com.example.storereservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public UserDto register(RegisterUser.Request request){
        PasswordUtils.validatePlainTextPassword(
                request.getPassword(), request.getPasswordCheck());

        if(userRepository.existsByUserId(request.getUserId())){
            throw new MyException(ErrorCode.DUPLICATED_ID);
        }

        request.setPassword(PasswordUtils.encPassword(request.getPassword()));

        UserEntity savedMember = userRepository.save(
                RegisterUser.Request.toEntity(request));

        log.info("User register complete : {}", savedMember);

        return UserDto.fromEntity(savedMember);
    }



}
