package com.example.storereservation.partner.service;

import com.example.storereservation.exception.ErrorCode;
import com.example.storereservation.exception.MyException;
import com.example.storereservation.partner.dto.PartnerDto;
import com.example.storereservation.partner.dto.RegisterPartner;
import com.example.storereservation.partner.entity.PartnerEntity;
import com.example.storereservation.partner.repository.PartnerRepository;
import com.example.storereservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerService{

    private final PartnerRepository partnerRepository;

    public PartnerDto register(RegisterPartner.Request request){
        PasswordUtils.validatePlainTextPassword(
                request.getPassword(), request.getPasswordCheck());

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
