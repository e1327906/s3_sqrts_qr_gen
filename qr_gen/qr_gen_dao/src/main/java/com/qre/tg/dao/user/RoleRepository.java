package com.qre.tg.dao.user;

import com.qre.tg.entity.user.Role;
import com.qre.tg.entity.user.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findRoleByName(RoleType name);
}
