package com.example.storereservation.web.controller;

import com.example.storereservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


//    /**
//     * 예약
//     */
//    @PostMapping("/reservation/{storeName}")
//    public ResponseEntity<?> reservation(@PathVariable String storeName){
//
//        return ResponseEntity.ok(null);
//    }


}
