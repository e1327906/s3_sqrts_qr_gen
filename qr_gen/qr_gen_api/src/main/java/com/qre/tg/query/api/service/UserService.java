
package com.qre.tg.query.api.service;

import com.qre.tg.dto.user.ChangePasswordRequest;
import com.qre.tg.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService{

    void changePassword(ChangePasswordRequest request);

    List<User> getAllUser();
}
