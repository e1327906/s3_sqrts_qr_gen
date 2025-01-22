package com.qre.tg.query.api.service.impl;

import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import com.qre.cmel.otp.sdk.service.OtpService;
import com.qre.tg.dao.token.TokenRepository;
import com.qre.tg.dao.user.RoleRepository;
import com.qre.tg.dao.user.UserRepository;
import com.qre.tg.dto.auth.AuthenticationRequest;
import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.token.Token;
import com.qre.tg.entity.token.TokenType;
import com.qre.tg.entity.user.Role;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.entity.user.User;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.config.JwtService;
import com.qre.tg.query.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository repository;

  private final RoleRepository roleRepository;

  private final TokenRepository tokenRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;

  private final OtpService otpService;

  private final EmailService emailService;

  private final ApplicationProperties applicationProperties;

  public AuthenticationResponse register(UserRequest userRequest) throws MessagingException, IOException {

    // Create a Set<Role> with the USER role
    Set<Role> roles = Collections.singleton(roleRepository.findRoleByName(RoleType.valueOf(userRequest.getRole())));

    var user = User.builder()
            .userName(userRequest.getUserName())
            .email(userRequest.getEmail())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .phoneNumber(userRequest.getPhoneNumber())
            .roles(roles)
            .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    CompletableFuture.runAsync(() -> {
      try {
        sendEmailOTP(userRequest);
      } catch (MessagingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .email(user.getEmail())
            .role(user.getRoles().stream().findFirst().get().getName().name())
            .userName(user.getUserName())
            .phoneNumber(user.getPhoneNumber())
            .userId(savedUser.getId().toString())
            .build();

  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .email(user.getEmail())
            .role(user.getRoles().stream().findFirst().get().getName().name())
            .userName(user.getUserName())
            .phoneNumber(user.getPhoneNumber())
            .userId(user.getId().toString())
            .build();
  }

  public void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  public void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public AuthenticationResponse refreshToken(HttpServletRequest request) {

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return null;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return authResponse;
      }
    }

    return null;
  }

  private void sendEmailOTP(UserRequest userRequest) throws MessagingException, IOException {
    int otp = otpService.generateOTP(userRequest.getEmail());
    String message = applicationProperties.getEmailMessage().replace("$", String.valueOf(otp));
    MessageDto messageDto = MessageDto.builder()
            .subject(applicationProperties.getEmailSubject())
            .message(message)
            .to(userRequest.getEmail())
            .build();
    emailService.send(messageDto);
  }

}
