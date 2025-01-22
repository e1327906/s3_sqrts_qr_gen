package com.qre.tg.query.api.service.impl;

import com.qre.tg.dao.ticket.TicketMasterRepository;
import com.qre.tg.dto.auth.AuthenticationRequest;
import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.qr.PurchaseTicketRequest;
import com.qre.tg.dto.qr.TicketDetailResponse;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.ticket.*;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.query.api.controller.impl.AuthenticationControllerImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    
    @Mock
    private TicketServiceImpl ticketService;

    @Mock
    private AuthenticationServiceImpl authService;

    @InjectMocks
    private AuthenticationControllerImpl authController;

    @Mock
    private TicketMasterRepository ticketMasterRepository;

    private PurchaseTicketRequest purchaseTicketRequest;

    private TicketMaster ticketMaster;

    private Long currentTime = new Date().getTime();
    private List<TicketDetailResponse> ticketDetailsList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        PurchaseTicketRequest purchaseTicketRequest = PurchaseTicketRequest.builder()
                .journeyType(1)
                .groupSize(0)
                .operatorId(3)
                .startDatetime(currentTime)
                .endDatetime(currentTime + 3600000) // Adding 1 hour for testing
                .departurePoint(123)
                .arrivalPoint(456)
                .paymentRefNo("PAY123")
                .amount(1000L)
                .currency("USD")
                .build();
        this.purchaseTicketRequest = purchaseTicketRequest;
        TicketMaster ticketMaster = TicketMaster.builder()
                .ticketType(purchaseTicketRequest.getTicketType())
                .journeyTypeId(purchaseTicketRequest.getJourneyType())
                .serialNumber("")
                .issuerId(10)
                .creatorId(5) //tg_id
                .groupSize(purchaseTicketRequest.getGroupSize())
                .creationDateTime(new Date())
                .effectiveDateTime(new Date())
                .operatorId(purchaseTicketRequest.getOperatorId()) // transport operator
                .validityDomain(1) // train/bus/flight/all
                .validityPeriod(ticketService.getValidityPeriod(new Date(purchaseTicketRequest.getStartDatetime()),
                        new Date(purchaseTicketRequest.getEndDatetime())))
                .transactionData(TransactionData.builder()
                        .paymentRefNo(purchaseTicketRequest.getPaymentRefNo())
                        .currency(purchaseTicketRequest.getCurrency())
                        .amount(purchaseTicketRequest.getAmount())
                        .build())
                .journeyDetails(ticketService.getJourneyDetails(purchaseTicketRequest))
                .additionalInfo(AdditionalInfo.builder().build())
                .security(mock(Security.class))
                .build();
        ticketMaster.setCreatedDatetime(new Date());
        this.ticketMaster =ticketMaster;
    }


    UserRequest authicateUser() throws MessagingException, IOException {
        // Given
        UserRequest userRequest = UserRequest.builder()
                .userName("test")
                .email("test@gmail.com")
                .phoneNumber("1122334455")
                .password("test")
                .role(RoleType.ROLE_USER.name())
                .build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .accessToken("fakeAccessToken")
                .refreshToken("fakeRefreshToken")
                .build();

        when(authService.register(userRequest)).thenReturn(mockResponse);

        // When
        ResponseEntity<AuthenticationResponse> response = authController.register(userRequest);

        AuthenticationRequest authReq = AuthenticationRequest.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        ResponseEntity<AuthenticationResponse> res = authController.authenticate(authReq);
        System.out.println(authReq);
        return userRequest;
    }


    @Test
    public void testGuestUserCanPurchaseTicketSuccessful() throws Exception{
        Map<String, Object> ticketJsonData = ticketService.prepareTicketJsonData(ticketMaster);
        ticketMaster.setQrData(ticketService.prepareQRData(ticketJsonData));
        ticketMaster.setEmail("guest@email.com");
        ticketMaster.setPhoneNo(null);
        when(ticketMasterRepository.save(any(TicketMaster.class))).thenReturn(ticketMaster);
        assertNotNull(ticketMaster);
    }

    @Test
    public void testRegiestedUserCanPurchaseTicketSuccessful() throws Exception{
        UserRequest registedUser =  authicateUser();
        Map<String, Object> ticketJsonData = ticketService.prepareTicketJsonData(ticketMaster);
        ticketMaster.setQrData(ticketService.prepareQRData(ticketJsonData));
        ticketMaster.setEmail(registedUser.getEmail());
        ticketMaster.setPhoneNo(registedUser.getPhoneNumber());
        when(ticketMasterRepository.save(any(TicketMaster.class))).thenReturn(ticketMaster);
        assertNotNull(ticketMaster);
    }

     @Test
     void testFindAllByEmail_ReturnTicketList() throws Exception {

         TicketDetailResponse res = TicketDetailResponse.builder()
                .serialNumber(ticketMaster.getSerialNumber())
                .qrData("")
                .effectiveDatetime(ticketMaster.getEffectiveDateTime().getTime())
                .journeyType(ticketMaster.getJourneyTypeId())
                .departurePoint(123)
                .arrivalPoint(456)
                .status(1)
                .arrivalPointDes("AA")
                .paymentRefNo(ticketMaster.getTransactionData().getPaymentRefNo())
                .amount(ticketMaster.getTransactionData().getAmount())
                .departurePointDes("")
                .build();
        ticketDetailsList.add(res);

        when(ticketService.findAllByEmail(authicateUser().getEmail())).thenReturn(ticketDetailsList);

         assertNotNull(ticketDetailsList);
         assertNotEquals(ticketDetailsList.size(),0);
         assertEquals(ticketDetailsList.size(),1);
     }

    @Test
    void testGetJourneyDetail_ReturnJourneyDetailList() throws Exception {
        Map<String, Object> ticketJsonData = ticketService.prepareTicketJsonData(ticketMaster);
        ticketMaster.setQrData(ticketService.prepareQRData(ticketJsonData));
        ticketMaster.setEmail(authicateUser().getEmail());
        ticketMaster.setPhoneNo(authicateUser().getPhoneNumber());
        when(ticketMasterRepository.save(any(TicketMaster.class))).thenReturn(ticketMaster);

        TicketDetailResponse res = TicketDetailResponse.builder()
                .serialNumber(ticketMaster.getSerialNumber())
                .qrData("")
                .effectiveDatetime(ticketMaster.getEffectiveDateTime().getTime())
                .journeyType(ticketMaster.getJourneyTypeId())
                .departurePoint(123)
                .arrivalPoint(456)
                .status(1)
                .arrivalPointDes("AA")
                .paymentRefNo(ticketMaster.getTransactionData().getPaymentRefNo())
                .amount(ticketMaster.getTransactionData().getAmount())
                .departurePointDes("")
                .build();
        ticketDetailsList.add(res);

        when(ticketService.findAllRefundableTicketByEmail(authicateUser().getEmail())).thenReturn(ticketDetailsList);
        assertNotNull(ticketDetailsList);
        assertNotEquals(ticketDetailsList.size(),0);
        assertEquals(ticketDetailsList.size(),1);
    }

    @Test
    void testGenerateSerialNumber() throws Exception {
        String sno = ticketMaster.getSerialNumber();
        when(ticketService.generateSerialNumber(purchaseTicketRequest)).thenReturn(sno);
        assertNotNull(sno);
    }

    @Test
    void testfindByTransactionDataPaymentRefNo() throws Exception {
        when(ticketService.findByTransactionDataPaymentRefNo(purchaseTicketRequest.getPaymentRefNo())).thenReturn(Optional.of(ticketMaster));
        assertNotNull(ticketMaster.getTransactionData());
    }
}