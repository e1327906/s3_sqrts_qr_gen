package com.qre.tg.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import com.qre.tg.entity.token.Token;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbFieldName.USERS,
        uniqueConstraints = @UniqueConstraint(columnNames = {DbFieldName.EMAIL}))
public class User extends AbstractPersistableEntity<UUID>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DbFieldName.USER_ID)
    private UUID userId;

    @Column(name = DbFieldName.USER_NAME)
    private String userName;

    @Column(name = DbFieldName.PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = DbFieldName.EMAIL, unique = true) // Add unique constraint
    private String email;

    @Column(name = DbFieldName.PASSWORD)
    @JsonIgnore
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = DbFieldName.USER_ROLES,
            joinColumns = @JoinColumn(name = DbFieldName.USER_ID),
            inverseJoinColumns = @JoinColumn(name = DbFieldName.ROLE_ID)
    )
    @JsonIgnore
    private Set<Role> roles;

    @OneToMany(mappedBy = DbFieldName.USER)
    @JsonIgnore
    private Set<Token> tokens;

    @Column(name = DbFieldName.REGISTER_WITH_EMAIL)
    private boolean registerWithEmail;

    @Column(name = DbFieldName.VERIFIED)
    @Builder.Default
    private boolean verified = false;

    @Override
    public UUID getId() {
        return userId;
    }
}
