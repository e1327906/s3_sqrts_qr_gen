package com.qre.tg.query.api.controller;

import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.query.api.controller.impl.AuthenticationControllerImpl;
import com.qre.tg.query.api.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationServiceImpl service;

    @InjectMocks
    private AuthenticationControllerImpl controller;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register_Operator_SuccessfulRegistration_ReturnsAccessTokenAndRefreshToken() throws MessagingException, IOException {
        // Given
        UserRequest request = UserRequest.builder()
                .userName("zaw")
                .email("zawoperator@gmail.com")
                .phoneNumber("3355667799")
                .password("P@ssw0rd123")
                .role(RoleType.ROLE_OPERATOR.name())
                .build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        when(service.register(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<AuthenticationResponse> response = controller.register(request);

        // Then
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotEquals("", response.getBody().getAccessToken(), "Access token should not be empty");
        assertNotEquals("", response.getBody().getRefreshToken(), "Refresh token should not be empty");
    }

    @Test
    void register_Admin_SuccessfulRegistration_ReturnsAccessTokenAndRefreshToken() throws MessagingException, IOException {
        // Given
        UserRequest request = UserRequest.builder()
                .userName("zaw")
                .email("zawadmin@gmail.com")
                .phoneNumber("3355667788")
                .password("P@ssw0rd123")
                .role(RoleType.ROLE_ADMIN.name())
                .build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        when(service.register(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<AuthenticationResponse> response = controller.register(request);

        // Then
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotEquals("", response.getBody().getAccessToken(), "Access token should not be empty");
        assertNotEquals("", response.getBody().getRefreshToken(), "Refresh token should not be empty");
    }

    @Test
    void register_SuccessfulRegistration_ReturnsAccessTokenAndRefreshToken() throws MessagingException, IOException {
        // Given
        UserRequest request = UserRequest.builder()
                .userName("zaw")
                .email("zaw@gmail.com")
                .phoneNumber("1122334455")
                .password("zaw")
                .role(RoleType.ROLE_USER.name())
                .build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .accessToken("fakeAccessToken")
                .refreshToken("fakeRefreshToken")
                .build();

        when(service.register(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<AuthenticationResponse> response = controller.register(request);

        // Then
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotEquals("", response.getBody().getAccessToken(), "Access token should not be empty");
        assertNotEquals("", response.getBody().getRefreshToken(), "Refresh token should not be empty");
    }

    @Test
    void register_InvalidRequest_ReturnsBadRequest() throws MessagingException, IOException {

        // Given
        UserRequest invalidRequest = UserRequest.builder().build();
        when(service.register(invalidRequest)).thenThrow(IllegalArgumentException.class);

        // When
        ResponseEntity<AuthenticationResponse> response = controller.register(invalidRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Should return HTTP Bad Request");
        assertNull(response.getBody(), "Response body should be null for invalid requests");
    }

    @Test
    void register_ServiceException_ReturnsInternalServerError() throws MessagingException, IOException {
        // Given
        UserRequest request = UserRequest.builder()
                .userName("zaw")
                .email("zaw@gmail.com")
                .phoneNumber("1122334455")
                .password("zaw")
                .role(RoleType.ROLE_USER.name())
                .build();

        when(service.register(request)).thenThrow(RuntimeException.class);
        // When
        ResponseEntity<AuthenticationResponse> response = controller.register(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Should return HTTP Internal Server Error");
        assertNull(response.getBody(), "Response body should be null for service exceptions");
    }

    @Test
    void register_NullResponse_ReturnsInternalServerError() throws MessagingException, IOException {
        // Given
        UserRequest request = UserRequest.builder()
                .userName("zaw")
                .email("zaw@gmail.com")
                .phoneNumber("1122334455")
                .password("zaw")
                .role(RoleType.ROLE_USER.name())
                .build();

        when(service.register(request)).thenThrow(NullPointerException.class);

        // When
        ResponseEntity<AuthenticationResponse> response = controller.register(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Should return HTTP Internal Server Error");
        assertNull(response.getBody(), "Response body should be null for null service response");
    }
}