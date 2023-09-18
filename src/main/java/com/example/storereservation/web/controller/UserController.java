package com.example.storereservation.web.controller;

import com.example.storereservation.user.service.UserService;
import com.example.storereservation.user.dto.RegisterUser;
import com.example.storereservation.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 유저 회원가입
     */
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser.Request request){
        UserDto registeredUser = userService.register(request);
        return ResponseEntity.ok(RegisterUser.Response.fromDto(registeredUser));
    }

//    /**
//     * 예약
//     */
//    @PostMapping("/reservation/{storeName}")
//    public ResponseEntity<?> reservation(@PathVariable String storeName){
//
//        return ResponseEntity.ok(null);
//    }



}
