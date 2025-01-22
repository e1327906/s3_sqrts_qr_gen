package com.qre.tg.entity.fare;

import com.qre.tg.entity.base.AbstractPersistableEntity;
import com.qre.tg.entity.base.DbFieldName;
import com.qre.tg.entity.route.Station;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbFieldName.TRAIN_FARE_MATRIX)
public class TrainFareMatrix extends AbstractPersistableEntity<TrainFareMatrixPK> {

    @EmbeddedId
    private TrainFareMatrixPK pk;

    @Column(name = DbFieldName.FARE)
    private long fare;

    @ManyToOne
    @JoinColumn(name = DbFieldName.SRC_STN_ID, referencedColumnName = DbFieldName.STN_ID, insertable = false, updatable = false)
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = DbFieldName.DEST_STN_ID, referencedColumnName = DbFieldName.STN_ID, insertable = false, updatable = false)
    private Station destinationStation;

    @Override
    public TrainFareMatrixPK getId() {
        return pk;
    }
}
