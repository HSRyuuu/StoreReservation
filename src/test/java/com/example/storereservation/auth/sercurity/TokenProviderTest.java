package com.example.storereservation.auth.sercurity;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;

    @Test
    void generateToken() {
        List<String> roleUser = new ArrayList<>(Arrays.asList("ROLE_USER"));
        String token = tokenProvider.generateToken("hello", roleUser);
        log.info("generateToken :  {}", token);
        String username = tokenProvider.getUsername(token);
        log.info("username : {}", username
        );


    }

    @Test
    void getUsername() {
    }

    @Test
    void validateToken() {
    }

    @Test
    void getAuthentication() {
    }
}