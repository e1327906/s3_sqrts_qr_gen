package com.qre.tg.query.api.controller.impl;
import com.qre.tg.dto.auth.AuthenticationRequest;
import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.qr.PurchaseTicketRequest;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.query.api.service.TicketService;
import com.qre.tg.query.api.service.impl.AuthenticationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketServiceControllerImplTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private AuthenticationServiceImpl authService;

    @InjectMocks
    private TicketServiceControllerImpl ticketServiceController;

    @InjectMocks
    private AuthenticationControllerImpl authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    public void testGuestUsercanPurchaseTicket() throws Exception {
        Long currentTime = new Date().getTime();

        PurchaseTicketRequest request = PurchaseTicketRequest.builder()
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
                .phoneNo("")
                .email("guest@gmail.com")
                .build();

        ResponseEntity<APIResponse> responseEntity = ticketServiceController.purchaseTicket(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(ticketService, times(1)).purchaseTicket(request);
    }

    @Test
    @Order(2)
    public void testRegisteredUsercanPurchaseTicket() throws Exception {
        Long currentTime = new Date().getTime();
        UserRequest userRequest = mock(UserRequest.class);
        AuthenticationResponse mockResponse = mock(AuthenticationResponse.class);

        AuthenticationRequest authReq = AuthenticationRequest.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
        when(authService.register(userRequest)).thenReturn(mockResponse);

        ResponseEntity<AuthenticationResponse> res = authController.authenticate(authReq);
        PurchaseTicketRequest request = PurchaseTicketRequest.builder()
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
                .phoneNo(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .build();

        ResponseEntity<APIResponse> responseEntity = ticketServiceController.purchaseTicket(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(ticketService, times(1)).purchaseTicket(request);
    }

    @Test
    @Order(3)
    public void testGetTickets() throws Exception {
        String email = "test@example.com";

        ResponseEntity<APIResponse> responseEntity = ticketServiceController.getTickets(email);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(ticketService, times(1)).findAllByEmail(email);
    }

    @Test
    @Order(4)
    public void testGetRefundTickets() throws Exception {
        String email = "test@example.com";

        ResponseEntity<APIResponse> responseEntity = ticketServiceController.getRefundTickets(email);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(ticketService, times(1)).findAllRefundableTicketByEmail(email);
    }
}