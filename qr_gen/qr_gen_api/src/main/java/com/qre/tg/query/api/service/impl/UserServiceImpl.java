
package com.qre.tg.query.api.service.impl;

import com.qre.tg.dao.user.RoleRepository;
import com.qre.tg.dao.user.UserRepository;
import com.qre.tg.dto.user.ChangePasswordRequest;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.dto.user.UserResponse;
import com.qre.tg.entity.user.Privilege;
import com.qre.tg.entity.user.Role;
import com.qre.tg.entity.user.User;
import com.qre.tg.query.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final CognitoIdentityProviderClient cognitoClient;

    @Value("${app.userPoolId}")
    private String userPoolId;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public void changePassword(ChangePasswordRequest request) {

        var exitingUser = userRepository.findByEmail(request.getEmail());

        if(exitingUser.isEmpty()){
            throw new IllegalStateException("Invalid user");
        }

        var user  = exitingUser.get();
        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<UserResponse> getAllCognitoUsers() {
        return listUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User name not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    public List<UserResponse> listUsers() {
        ListUsersResponse response = cognitoClient.listUsers(
                ListUsersRequest.builder().userPoolId(userPoolId).build()
        );

        return response.users().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(UserType user) {
        // Convert attribute list to a map for easy lookup
        Map<String, String> attributesMap = user.attributes().stream()
                .collect(Collectors.toMap(AttributeType::name, AttributeType::value));

        return UserResponse.builder()
                .userId(user.username())
                .userName(attributesMap.get("email").replaceAll("@.*", ""))
                .email(attributesMap.get("email")) // Cognito stores email under "email"
                .phoneNumber(attributesMap.get("phone_number")) // Cognito stores phone number under "phone_number"
                .build();
    }

    // Fetch groups from Cognito
    public List<String> listGroups() {
        ListGroupsResponse response = cognitoClient.listGroups(
                ListGroupsRequest.builder().userPoolId(userPoolId).build()
        );

        return response.groups().stream()
                .map(GroupType::groupName)
                .collect(Collectors.toList());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Set<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName().name());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getPrivilege());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
