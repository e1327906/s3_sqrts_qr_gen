package com.qre.tg.entity.fare;

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
@Table(name = DbFieldName.BUS_FARE_MATRIX)
public class BusFareMatrix extends AbstractPersistableEntity<BusFareMatrixPK> {

    @EmbeddedId
    private BusFareMatrixPK pk;

    @Column(name = DbFieldName.FARE)
    private long fare;

    @Override
    public BusFareMatrixPK getId() {
        return pk;
    }
}