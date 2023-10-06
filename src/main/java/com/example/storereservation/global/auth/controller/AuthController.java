package com.example.storereservation.global.auth.controller;

import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.auth.dto.AuthResponse;
import com.example.storereservation.global.auth.dto.LoginInput;
import com.example.storereservation.global.auth.sercurity.TokenProvider;
import com.example.storereservation.global.auth.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    /**
     * 유저 로그인
     */
    @ApiOperation(value = "유저 로그인", notes = "로그인 시 id와 JWT 토큰이 반환된다.")
    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginInput input) {
        UserEntity loginUser = authService.authenticateUser(input);

        String token = tokenProvider.generateToken(loginUser.getUsername(), new ArrayList<>(Collections.singletonList(loginUser.getMemberType())));

        log.info("[LOGIN] ID={}, ROLE={}", loginUser.getUserId(), loginUser.getMemberType());

        return ResponseEntity.ok(new AuthResponse(loginUser.getUserId(), token));
    }

    /**
     *  파트너 로그인
     */
    @ApiOperation(value = "파트너 로그인", notes = "로그인 시 id와 JWT 토큰이 반환된다.")
    @PostMapping("/partner/login")
    public ResponseEntity<?> partnerLogin(@RequestBody LoginInput input) {
        PartnerEntity loginPartner = authService.authenticatePartner(input);

        String token = tokenProvider.generateToken(loginPartner.getUsername(), new ArrayList<>(Collections.singletonList(loginPartner.getMemberType())));

        log.info("[LOGIN] ID={}, ROLE={}", loginPartner.getPartnerId(), loginPartner.getMemberType());

        return ResponseEntity.ok(new AuthResponse(loginPartner.getPartnerId(), token));
    }


}
