package com.example.storereservation.partner.service;

import com.example.storereservation.exception.ErrorCode;
import com.example.storereservation.exception.MyException;
import com.example.storereservation.partner.entity.PartnerEntity;
import com.example.storereservation.partner.repository.PartnerRepository;
import com.example.storereservation.util.PasswordUtils;
import com.example.storereservation.partner.dto.PartnerDto;
import com.example.storereservation.partner.dto.RegisterPartner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerDto register(RegisterPartner.Request partner){
        PasswordUtils.validatePlainTextPassword(
                partner.getPassword(), partner.getPasswordCheck());

        if(partnerRepository.existsByPartnerId(partner.getPartnerId())){
            throw new MyException(ErrorCode.DUPLICATED_ID);
        }

        PartnerEntity savedManager = partnerRepository.save(
                RegisterPartner.Request.toEntity(partner));
        log.info("Manager register complete : {}", savedManager);

        return PartnerDto.fromEntity(savedManager);
    }

}
