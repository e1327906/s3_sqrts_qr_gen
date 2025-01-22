package com.qre.tg.query.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qre.cmel.security.component.Crypto;
import com.qre.tg.dao.ticket.JourneyDetailsRepository;
import com.qre.tg.dao.ticket.TicketMasterRepository;
import com.qre.tg.dto.qr.ValidationRequest;
import com.qre.tg.entity.ticket.JourneyDetails;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.common.QRData;
import com.qre.tg.query.api.common.TicketStatusEnum;
import com.qre.tg.query.api.config.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;

public class QRValidatorServiceImplTest {


    @Mock
    private TicketMasterRepository ticketMasterRepository;

    @Mock
    private JourneyDetailsRepository journeyDetailsRepository;

    @InjectMocks
    private QRValidatorServiceImpl qrValidatorService;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private Crypto crypto;

    @Mock
    private  JourneyDetails journeyDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
     }

    @Test
    public void testValidate_EntryStatus() throws Exception {
        // Arrange
        ValidationRequest request = new ValidationRequest();

        String qrdata = "sHyPQkSN0IT7eJwVc02HgNzZoAn+ktURo13V19Z2HuxZlsJLlxa3RVzDQeWEpTbN2IuGqHJvsXO2WO67Buj661ti0ZA09VDca5PB+a74d2e0SbjjuGjQrWTTCp2UCa4ydkMXGjzsUhKSFPw0UK75/zFnr/s50o9ibuuzJ3ireF4Azr/os4tChi7qezV5ouQckOir/FB64g6oH6bkiohmwSbYnd8Pa/wBj5hYw9Xia1VUCSZheUmMcR/oiud+Ke6x+7FEibkt6E1LAudcEQJjXL2nTuyZIzbZNdrCIVONSM3hmXiq7gkvDr/+0qkJ7wLnIYETO8hxyDeSUMlpLSggdg==#ugQq3LrIs5cSlzP5fVpfGaMzBAPvib5e8wd68RyX5MSsoRiKzo6YN918qkk8FNTsk2QnMKHWmKBt5nEJhI87Qrp9KrvlMQZZgz6+as/RQn3nl5QBuLrmmNjRLv/LVpwB0UAoj8pzifKzMdgroMLFGpY6TMpSal0bB8Wgy/VwlbR6hX3glRmi9Ro+lpCM7WXwZprmzkCom2Zf7MogdbO3k596H7fYjnpXBOw69PQdDu40DRFYizM2rMsSReqCX2dBgtfWFyzbe36ulCx3dQqrYC5ckp3yEtwWorlxY22f/DVivIRhidvwXrrR2fEZwm10QfjBbeviCzCTOqmFZs+dQW18cUnmvSrK3ZTBZBzGiSMoVyCsTLBVKR9t0Uvm2nBsRdazy+C+nWpcUu2qzVwxzwRi42LkiqE0RMMd/ATEGe5UKXcdCoRHIyhlM79FqcQBScIMC79W5J/UqqQAb4A1hXHn+rFQGyVD/JxikN1oHaSReV33ps9Hv+2Ki5fFO8Kk";

        request.setQrData(qrdata);
        request.setStatus(TicketStatusEnum.ENTRY.getValue());
        request.setEntryDateTime(System.currentTimeMillis());

        TicketMaster ticketMaster = buildTicketMaster();

        when(ticketMasterRepository.findAllBySerialNumber(any())).thenReturn(Optional.of(ticketMaster));
        when(crypto.aesDecrypt(any(),any())).thenReturn("{\"ticketDetail\":{\"validityDomain\":1,\"serialNumber\":\"2024040516293910WEB0000000000001\",\"creatorId\":5,\"transactionData\":{\"paymentRefNo\":\"pi_3P2FdoFcp66ilBOo0zMWqaMB\",\"amount\":10,\"currency\":\"sgd\"},\"ticketType\":1,\"phoneNo\":\"1122334455\",\"validityPeriod\":0,\"issuerId\":10,\"journeyType\":1,\"effectiveDateTime\":1712363340000,\"groupSize\":1,\"operatorId\":0,\"email\":\"insaneappcreator@gmail.com\"}}");
        when(journeyDetailsRepository.save(any())).thenReturn(ticketMaster.getJourneyDetails().get(0));

        // Act
        qrValidatorService.validate(request);

        // Assert
        verify(journeyDetailsRepository, times(1)).save(any());
    }

    private TicketMaster buildTicketMaster() {
        TicketMaster ticketMaster = TicketMaster.builder()
                .ticketId(UUID.randomUUID())
                .serialNumber("123")
                .journeyDetails(Collections.singletonList(
                    JourneyDetails.builder()
                            .status(1)
                            .build()
                ))
                .build();
        return ticketMaster;
    }

    // Similar tests for other cases like EXIT status, invalid QR data, etc.
}
