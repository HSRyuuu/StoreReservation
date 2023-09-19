package com.example.storereservation.domain.partner.service;

import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import com.example.storereservation.domain.partner.dto.PartnerDto;
import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.persist.PartnerRepository;
import com.example.storereservation.global.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerService{

    private final PartnerRepository partnerRepository;

    public PartnerDto register(RegisterPartner.Request request){
        if(!PasswordUtils.validatePlainTextPassword(
                request.getPassword(), request.getPasswordCheck())){
            throw new MyException(ErrorCode.PASSWORD_CHECK_INCORRECT);
        }

        if(partnerRepository.existsByPartnerId(request.getPartnerId())){
            throw new MyException(ErrorCode.DUPLICATED_ID);
        }

        request.setPassword(PasswordUtils.encPassword(request.getPassword()));

        PartnerEntity savedManager = partnerRepository.save(
                RegisterPartner.Request.toEntity(request));
        log.info("Manager register complete : {}", savedManager);

        return PartnerDto.fromEntity(savedManager);
    }





}
