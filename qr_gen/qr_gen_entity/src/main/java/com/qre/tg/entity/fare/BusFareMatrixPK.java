package com.qre.tg.entity.fare;

import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BusFareMatrixPK implements Serializable {
    @Column(name = DbFieldName.SRC_BUS_STOP_ID)
    private Integer srcBusStopId;

    @Column(name = DbFieldName.DEST_BUS_STOP_ID)
    private Integer destBusStopId;

    @Column(name = DbFieldName.TICKET_TYPE_ID)
    private Integer ticketTypeId;
}
