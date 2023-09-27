package com.example.storereservation.global.auth.service;

import com.example.storereservation.global.auth.dto.LoginInput;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.persist.PartnerRepository;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.domain.user.persist.UserRepository;
import com.example.storereservation.global.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    /**
     * User auth
     */
    public UserEntity authenticateUser(LoginInput input) {
        UserEntity user = userRepository.findByUserId(input.getUsername())
                .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));
        if (!PasswordUtils.equals(input.getPassword(), user.getPassword())) {
            throw new MyException(ErrorCode.PASSWORD_INCORRECT);
        }
        return user;
    }

    /**
     * Partner auth
     */
    public PartnerEntity authenticatePartner(LoginInput input) {
        PartnerEntity partner = partnerRepository.findByPartnerId(input.getUsername())
                .orElseThrow(() -> new MyException(ErrorCode.PARTNER_NOT_FOUND));
        if (!PasswordUtils.equals(input.getPassword(), partner.getPassword())) {
            throw new MyException(ErrorCode.PASSWORD_INCORRECT);
        }
        return partner;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Load User => USERID : {}", username);
        if (userRepository.existsByUserId(username)) {
            return userRepository.findByUserId(username)
                    .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));
        } else if (partnerRepository.existsByPartnerId(username)) {
            return partnerRepository.findByPartnerId(username)
                    .orElseThrow(() -> new MyException(ErrorCode.PARTNER_NOT_FOUND));
        }
        log.error("AuthService -> loadUserByUsername FAILED");
        return null;
    }

}
