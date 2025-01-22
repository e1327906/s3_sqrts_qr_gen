package com.qre.tg.entity.base;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractPersistableEntity<T extends Serializable>
        implements Persistable<T> {

    @Column(name = DbFieldName.CREATED_DATETIME)
    @Temporal(TemporalType.TIMESTAMP)
    Date createdDatetime;

    @Column(name = DbFieldName.CREATED_USER)
    String createdUser;

    @Column(name = DbFieldName.UPDATED_DATETIME)
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedDatetime;

    @Column(name = DbFieldName.UPDATED_USER)
    String updatedUser;

    @Column(name = DbFieldName.TERMINAL_ID)
    String terminalId;

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
