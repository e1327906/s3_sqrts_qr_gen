package com.qre.tg.entity.sysconfig;

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
@Table(name = DbFieldName.SYSTEM_CONFIG)
public class SystemConfig extends AbstractPersistableEntity<String> {

    @Id
    @Column(name = DbFieldName.PROP_ID)
    private String propertyId;

    @Column(name = DbFieldName.PROP_VALUE)
    private String propertyValue;

    @Column(name = DbFieldName.PROP_DESC)
    private String propertyDesc;

    @Column(name = DbFieldName.MODIFY_IND)
    private Short modifyInd;

    @Override
    public String getId() {
        return propertyId;
    }
}
