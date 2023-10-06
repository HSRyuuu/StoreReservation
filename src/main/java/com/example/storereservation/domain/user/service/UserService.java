package com.example.storereservation.domain.user.service;

import com.example.storereservation.domain.user.dto.RegisterUser;
import com.example.storereservation.domain.user.dto.UserDto;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.domain.user.persist.UserRepository;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import com.example.storereservation.global.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService{

    private final UserRepository userRepository;

    public UserDto register(RegisterUser.Request request){
        if(!PasswordUtils.validatePlainTextPassword(
                request.getPassword(), request.getPasswordCheck())){
            throw new MyException(ErrorCode.PASSWORD_CHECK_INCORRECT);
        }


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
