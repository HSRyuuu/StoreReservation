package com.example.storereservation.web.controller;

import com.example.storereservation.auth.dto.AuthResponse;
import com.example.storereservation.auth.dto.LoginInput;
import com.example.storereservation.auth.sercurity.TokenProvider;
import com.example.storereservation.auth.service.AuthService;
import com.example.storereservation.partner.dto.PartnerDto;
import com.example.storereservation.partner.dto.RegisterPartner;
import com.example.storereservation.partner.entity.PartnerEntity;
import com.example.storereservation.partner.service.PartnerService;
import com.example.storereservation.user.dto.RegisterUser;
import com.example.storereservation.user.dto.UserDto;
import com.example.storereservation.user.entity.UserEntity;
import com.example.storereservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PartnerService partnerService;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    /**
     * 유저 회원가입
     */
    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser.Request request) {
        UserDto registeredUser = userService.register(request);
        return ResponseEntity.ok(RegisterUser.Response.fromDto(registeredUser));
    }

    /**
     * 유저 로그인
     */
    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginInput input) {
        UserEntity loginUser = authService.authenticateUser(input);

        String token = tokenProvider.generateToken(loginUser.getUsername(), new ArrayList<>(Collections.singletonList(loginUser.getMemberType())));

        log.info("[LOGIN] ID={}, ROLE={}", loginUser.getUserId(), loginUser.getMemberType());

        return ResponseEntity.ok(new AuthResponse(loginUser.getUserId(), token));
    }

    /**
     * 파트너 회원가입
     */
    @PostMapping("/partner/register")
    public ResponseEntity<?> registerPartner(@RequestBody RegisterPartner.Request request) {
        PartnerDto registeredManager = partnerService.register(request);

        return ResponseEntity.ok(RegisterPartner.Response.fromDto(registeredManager));
    }

    /**
     * 유저 로그인
     */
    @PostMapping("/partner/login")
    public ResponseEntity<?> partnerLogin(@RequestBody LoginInput input) {
        PartnerEntity loginPartner = authService.authenticatePartner(input);

        String token = tokenProvider.generateToken(loginPartner.getUsername(), new ArrayList<>(Collections.singletonList(loginPartner.getMemberType())));

        log.info("[LOGIN] ID={}, ROLE={}", loginPartner.getPartnerId(), loginPartner.getMemberType());

        return ResponseEntity.ok(new AuthResponse(loginPartner.getPartnerId(), token));
    }


}
