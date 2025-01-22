package com.qre.tg.query.api.service.impl;

import com.qre.tg.dao.user.RoleRepository;
import com.qre.tg.dao.user.UserRepository;
import com.qre.tg.dto.user.ChangePasswordRequest;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.user.Privilege;
import com.qre.tg.entity.user.Role;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.entity.user.User;
import com.qre.tg.query.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void changePassword() throws MessagingException, IOException {

        Role role = new Role();
        role.setName(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Execute
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .email("thinyadanasu86@gmail.com")
                .currentPassword("admin")
                .newPassword("Thin@1234")
                .confirmationPassword("Thin@1234")
                .build();

        User user = User.builder()
                .userName("Thin")
                .email("thinyadanasu86@gmail.com")
                .phoneNumber("91804035")
                .password("$2a$10$Y3x.RsVjAq3K3Lk2lKtmfenT8y4siJm.ZVubAhex3YQ3I6EijEpF.")
                .roles(roles)
                .build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);

        userService.changePassword(request);

        // Verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(any(CharSequence.class), anyString());
        verify(userRepository, times(1)).save(user);

        // Add error handling
        try {
            verify(userRepository, never()).findByEmail("nonexistent@gmail.com"); // Ensure findByEmail is not called with non-existent email
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String email = "thinyadanasu86@gmail.com";
        String password = "Thin@2024";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Role role = new Role();
        role.setName(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        Privilege p =new Privilege();
        p.setPrivilege("privilege");
        collection.add(p);
        role.setPrivileges(collection);
        user.setRoles(roles);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(email);

        // Assert
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        //assertEquals(Collections.emptyList(), userDetails.getAuthorities());

        // Verify interactions
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));

        // Verify interactions
        verify(userRepository, times(1)).findByEmail(email);
    }
}
