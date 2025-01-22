package com.qre.tg.query.api.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qre.cmel.exception.EntryExitMismatchException;
import com.qre.cmel.exception.InvalidTicketException;
import com.qre.cmel.security.component.Crypto;
import com.qre.tg.dao.ticket.JourneyDetailsRepository;
import com.qre.tg.dao.ticket.TicketMasterRepository;
import com.qre.tg.dto.qr.ValidationRequest;
import com.qre.tg.entity.ticket.JourneyDetails;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.common.QRData;
import com.qre.tg.query.api.common.TicketStatusEnum;
import com.qre.tg.query.api.common.ValidationData;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.service.QRValidatorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QRValidatorServiceImpl implements QRValidatorService {


    private final Logger logger = LoggerFactory.getLogger(QRValidatorServiceImpl.class);

    private final Crypto crypto;

    private final ApplicationProperties applicationProperties;

    private final TicketMasterRepository ticketMasterRepository;

    private final JourneyDetailsRepository journeyDetailsRepository;

    @Override
    public void validate(ValidationRequest request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String[] dataList = request.getQrData().split("#");
        if (dataList.length < 2) {
            throw new IllegalArgumentException("Invalid QR data");
        }

        //String digitalSignature = dataList[0];
        String qrData = dataList[1];
        byte[] decodedQRData = Base64.getDecoder().decode(qrData);
        String decryptData = crypto.aesDecrypt(decodedQRData, applicationProperties.getPrivateKeyPath());
        logger.info("QRValidatorServiceImpl.validate decryptData : {}", decryptData);

        ValidationData validationData = mapper.readValue(decryptData, ValidationData.class);
        QRData qrValidationData = validationData.getQRData();

        Optional<TicketMaster> ticketMasterOpt = ticketMasterRepository.findAllBySerialNumber(qrValidationData.getSerialNumber());
        if (ticketMasterOpt.isEmpty()) {
            throw new InvalidTicketException("Invalid ticket[" + qrValidationData.getSerialNumber() + "]");
        }

        TicketMaster ticketMaster = ticketMasterOpt.get();
        Optional<JourneyDetails> journeyDetailsOpt = ticketMaster.getJourneyDetails().stream().findFirst();
        if (journeyDetailsOpt.isEmpty()) {
            throw new InvalidTicketException("Invalid journey of ticket [" + qrValidationData.getSerialNumber() + "]");
        }

        JourneyDetails journeyDetails = journeyDetailsOpt.get();
        if (request.getStatus().equals(TicketStatusEnum.ENTRY.getValue())) {
            if (!journeyDetails.getStatus().equals(TicketStatusEnum.ACTIVE.getValue())) {
                throw new EntryExitMismatchException("Ticket [" + qrValidationData.getSerialNumber() + "] is not available for entry");
            }
            journeyDetails.setEntryDateTime(new Date(request.getEntryDateTime()));
            journeyDetails.setStatus(request.getStatus());
            journeyDetails.setUpdatedDatetime(new Date());
        } else if (request.getStatus().equals(TicketStatusEnum.EXIT.getValue())) {
            if (!journeyDetails.getStatus().equals(TicketStatusEnum.ENTRY.getValue())) {
                throw new EntryExitMismatchException("Ticket [" + qrValidationData.getSerialNumber() + "] is not available for exit");
            }
            journeyDetails.setExitDateTime(new Date(request.getExitDateTime()));
            journeyDetails.setStatus(request.getStatus());
            journeyDetails.setUpdatedDatetime(new Date());
        } else {
            throw new EntryExitMismatchException("Invalid ticket status: " + request.getStatus());
        }
        journeyDetailsRepository.save(journeyDetails);
    }
}
