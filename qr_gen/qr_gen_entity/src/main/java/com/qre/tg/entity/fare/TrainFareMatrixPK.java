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
public class TrainFareMatrixPK implements Serializable {

    @Column(name = DbFieldName.SRC_STN_ID)
    private Integer srcStnId;

    @Column(name = DbFieldName.DEST_STN_ID)
    private Integer destStnId;

    @Column(name = DbFieldName. TICKET_TYPE_ID)
    private Integer ticketTypeId;

}
