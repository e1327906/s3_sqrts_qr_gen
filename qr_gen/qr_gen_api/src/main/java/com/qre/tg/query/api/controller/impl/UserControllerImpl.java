package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.user.ChangePasswordRequest;
import com.qre.tg.query.api.config.LogoutService;
import com.qre.tg.query.api.controller.UserController;
import com.qre.tg.query.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService service;
    private final LogoutService logoutService;

    @PostMapping("/ChangePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request);
        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("/Logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response, null); // Assuming no authentication object is needed for logout
        // You can customize the response or add additional logic as needed
    }

    @GetMapping("/GetUsers")
    @Override
    public ResponseEntity<APIResponse> getAllUser() {
        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .responseData(service.getAllUser())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
