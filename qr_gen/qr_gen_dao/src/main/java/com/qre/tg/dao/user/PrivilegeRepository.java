package com.qre.tg.dao.user;

import com.qre.tg.entity.user.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

    Privilege findByPrivilege(String name);

    @Override
    void delete(Privilege privilege);
}
