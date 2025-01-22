package com.qre.tg.entity.user;

import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRolePK implements Serializable {

    private UUID userId;

    private UUID roleId;
}
