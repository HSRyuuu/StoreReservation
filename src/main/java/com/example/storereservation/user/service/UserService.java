package com.example.storereservation.user.service;

import com.example.storereservation.exception.ErrorCode;
import com.example.storereservation.exception.MyException;
import com.example.storereservation.user.entity.UserEntity;
import com.example.storereservation.user.repository.UserRepository;
import com.example.storereservation.util.PasswordUtils;
import com.example.storereservation.user.dto.UserDto;
import com.example.storereservation.user.dto.RegisterUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public UserDto register(RegisterUser.Request user){
        PasswordUtils.validatePlainTextPassword(
                user.getPassword(), user.getPasswordCheck());

        if(userRepository.existsByUserId(user.getUserId())){
            throw new MyException(ErrorCode.DUPLICATED_ID);
        }

        UserEntity savedMember = userRepository.save(
                RegisterUser.Request.toEntity(user));

        log.info("User register complete : {}", savedMember);

        return UserDto.fromEntity(savedMember);
    }



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
}
