package com.qre.tg.query.api.service;

import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import com.qre.cmel.otp.sdk.service.OtpService;
import com.qre.tg.dao.token.TokenRepository;
import com.qre.tg.dao.user.RoleRepository;
import com.qre.tg.dao.user.UserRepository;
import com.qre.tg.dto.auth.AuthenticationRequest;
import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.user.Role;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.entity.user.User;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.config.JwtService;
import com.qre.tg.query.api.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private OtpService otpService;

    @Mock
    private EmailService emailService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void register_SuccessfulReturnsAccessTokenAndRefreshToken() throws MessagingException, IOException {
        // Given
        UserRequest userRequest = UserRequest.builder()
                .userName("zaw")
                .email("zawadmin@gmail.com")
                .phoneNumber("1122334455")
                .password("zaw")
                .role(RoleType.ROLE_USER.name())
                .build();
        Role role = new Role();
        role.setName(RoleType.ROLE_USER);
        when(roleRepository.findRoleByName(RoleType.ROLE_USER)).thenReturn(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .userId(UUID.randomUUID())
                .roles(roles)
                .build();
        // Stubbing userRepository.save() to accept any User object
        when(userRepository.save(any(User.class))).thenReturn(user);

        when(applicationProperties.getEmailMessage()).thenReturn("Dear User, your OTP for registration is $. Use this code to validate your login");
        //when(applicationProperties.getEmailSubject()).thenReturn("SQRT OTP");

        // When
        AuthenticationResponse authenticationResponse = service.register(userRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(user.getEmail(), savedUser.getEmail());
        assertNotNull(authenticationResponse);
    }


    @Test
    void register_UserWithExistingEmail() {
        // Given
        UserRequest userRequest = UserRequest.builder()
                .userName("zaw")
                .email("zawadmin@gmail.com")
                .phoneNumber("1122334455")
                .password("zaw")
                .role(RoleType.ROLE_USER.name())
                .build();

        // Stubbing userRepository.save() to throw DataIntegrityViolationException
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        //When&Then
        assertThrows(DataIntegrityViolationException.class, () -> service.register(userRequest));

    }

    @Test
    void register_UserWithInvalidUserRequestData() {
        // Given
        UserRequest userRequest = UserRequest.builder()
                .role(RoleType.ROLE_USER.name())
                .build();
        // Stubbing userRepository.save() to throw DataIntegrityViolationException
        when(userRepository.save(any(User.class))).thenThrow(IllegalArgumentException.class);

        //When&Then
        assertThrows(IllegalArgumentException.class, () -> service.register(userRequest));

    }

    @Test
    void testAuthenticate() {
        // Mock AuthenticationRequest
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");

        // Mock Authentication and User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUserName("testUser");
        Role role = Role.builder()
                .name(RoleType.ROLE_USER)
                .build();
        Set<Role> roles = Collections.singleton(role);
        user.setRoles(roles);
        user.setUserId(UUID.randomUUID());

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock repository and jwtService methods
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockedToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("mockedRefreshToken");

        // Call the authenticate method
        AuthenticationResponse response = service.authenticate(request);

        // Verify interactions
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(userRepository).findByEmail(request.getEmail());
        verify(jwtService).generateToken(user);
        verify(jwtService).generateRefreshToken(user);

        // Assertions
        assertNotNull(response);
        assertEquals("mockedToken", response.getAccessToken());
        assertEquals("mockedRefreshToken", response.getRefreshToken());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());
        assertEquals("testUser", response.getUserName());
    }

}