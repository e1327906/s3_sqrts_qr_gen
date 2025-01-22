package com.qre.tg.entity.ticket;

import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbFieldName.TICKET_ISSUER)
public class TicketIssuer extends AbstractPersistableEntity<Integer>{

    @Id
    @Column(name = DbFieldName.ISSUER_ID)
    private int issuerId;

    @Column(name = DbFieldName.ISSUER)
    private String issuer;

    @Column(name = DbFieldName.APP_TYPE)
    private String appType;

    @Override
    public Integer getId() {
        return issuerId;
    }
}
