package com.qre.tg.query.api.component;

import com.qre.tg.dao.user.PrivilegeRepository;
import com.qre.tg.dao.user.RoleRepository;
import com.qre.tg.dao.user.UserRepository;
import com.qre.tg.entity.user.Privilege;
import com.qre.tg.entity.user.Role;
import com.qre.tg.entity.user.RoleType;
import com.qre.tg.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege operatorPrivilege = createPrivilegeIfNotFound("READ_WRITE_PRIVILEGE");
        final Privilege adminPrivilege = createPrivilegeIfNotFound("FULL_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege,operatorPrivilege, adminPrivilege));
        final List<Privilege> operatorPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege,operatorPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, adminPrivilege));
        final Role adminRole = createRoleIfNotFound(RoleType.valueOf("ROLE_ADMIN"), adminPrivileges);
        createRoleIfNotFound(RoleType.valueOf("ROLE_USER"), userPrivileges);
        createRoleIfNotFound(RoleType.valueOf("ROLE_OPERATOR"), operatorPrivileges);

        // == create initial user
        Set<Role> roles = Collections.singleton(adminRole);
        createUserIfNotFound("admin@nus.com", "admin", "admin",
                roles);

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByPrivilege(name);
        if (privilege == null) {
            privilege = Privilege.builder()
                    .privilege(name)
                    .build();
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(final RoleType name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findRoleByName(name);
        if (role == null) {
            role = Role.builder()
                    .name(name)
                    .build();
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    public User createUserIfNotFound(final String email, final String userName, final String password, final Set<Role> roles) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = User.builder()
                    .userName(userName)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .roles(roles)
                    .build();

        }

        user = userRepository.save(user);
        return user;
    }

}