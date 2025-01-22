package com.qre.tg.entity.route;

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
@Table(name = DbFieldName.BUS_STOP)
public class BusStop extends AbstractPersistableEntity<Integer> {

    @Id
    @Column(name = DbFieldName.BUS_STOP_ID)
    private int busStopId;

    @Column(name = DbFieldName.BUS_STOP_CODE)
    private String busStopCode;

    @Column(name = DbFieldName.BUS_STOP_FULL_NAME)
    private String busStopFullName;

    @Override
    public Integer getId() {
        return busStopId;
    }
}
