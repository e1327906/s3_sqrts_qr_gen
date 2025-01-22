package com.qre.tg.entity.ticket;

import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbFieldName.JOURNEY_DETAILS)
public class JourneyDetails extends AbstractPersistableEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DbFieldName.JOURNEY_ID)
    private UUID journeyId;

    @Column(name = DbFieldName.DEPARTURE_POINT)
    private Integer departurePoint;

    @Column(name = DbFieldName.ARRIVAL_POINT)
    private Integer arrivalPoint;

    @Column(name = DbFieldName.ENTRY_DATETIME)
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDateTime;

    @Column(name = DbFieldName.EXIT_DATETIME)
    @Temporal(TemporalType.TIMESTAMP)
    private Date exitDateTime;

    @Column(name = DbFieldName.STATUS)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = DbFieldName.TICKET_ID)
    private TicketMaster ticketMaster;

    @Override
    public UUID getId() {
        return journeyId;
    }
}
