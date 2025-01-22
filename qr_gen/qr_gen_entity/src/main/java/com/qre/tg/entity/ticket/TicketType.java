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
@Table(name = DbFieldName.TICKET_TYPES)
public class TicketType extends AbstractPersistableEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DbFieldName.TICKET_TYPE_ID)
    private int ticketTypeId;

    @Column(name = DbFieldName.TICKET_TYPE_NAME)
    private String ticketTypeName;

    @Override
    public Integer getId() {
        return ticketTypeId;
    }
}
