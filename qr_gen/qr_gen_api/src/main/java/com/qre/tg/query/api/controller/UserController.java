package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.user.ChangePasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface UserController {

    ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser);

    void logout(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<APIResponse> getAllUser();

    ResponseEntity<APIResponse> getAllCognitoUsers();

}