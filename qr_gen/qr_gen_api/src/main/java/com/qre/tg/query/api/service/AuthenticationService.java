package com.qre.tg.query.api.service;

import com.qre.tg.dto.auth.AuthenticationRequest;
import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.user.User;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register(UserRequest userRequest) throws MessagingException, IOException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void saveUserToken(User user, String jwtToken);

    void revokeAllUserTokens(User user);

    AuthenticationResponse refreshToken(HttpServletRequest request);
}
