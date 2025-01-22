package com.qre.tg.entity.user;

import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = DbFieldName.PRIVILEGES,
        uniqueConstraints = @UniqueConstraint(columnNames = {DbFieldName.PRIVILEGE}))
public class Privilege extends AbstractPersistableEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DbFieldName.PRIVILEGE_ID)
    private UUID privilege_id;

    @Column(name = DbFieldName.PRIVILEGE, unique = true)
    private String privilege;

    @ManyToMany(mappedBy = DbFieldName.PRIVILEGES)
    private Collection<Role> roles;

    @Override
    public UUID getId() {
        return privilege_id;
    }
}