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
@Table(name = DbFieldName.STATION)
public class Station extends AbstractPersistableEntity<Integer> {

    @Id
    @Column(name = DbFieldName.STN_ID)
    private int stnId;

    @Column(name = DbFieldName.STN_CODE)
    private String stnCode;

    @Column(name = DbFieldName.STN_FULL_NAME)
    private String stnFullName;

    @Override
    public Integer getId() {
        return stnId;
    }
}

