package com.qre.tg.entity.token;

import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import com.qre.tg.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbFieldName.TOKENS)
public class Token extends AbstractPersistableEntity<UUID> {

    @Id
    @GeneratedValue
    @Column(name = DbFieldName.TOKEN_ID, insertable = false)
    private UUID tokenId;

    @Column(name= DbFieldName.TOKEN, nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    @Column(name = DbFieldName.REVOKED)
    private boolean revoked;

    @Column(name = DbFieldName.EXPIRED)
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DbFieldName.USER_ID)
    private User user;

    @Override
    public UUID getId() {
        return tokenId;
    }
}
