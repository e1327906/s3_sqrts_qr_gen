package com.qre.tg.entity.ticket;

import com.qre.tg.entity.base.DbFieldName;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfo implements Serializable {

    @Column(name = DbFieldName.CUSTOM_FIELD_1)
    private String customField1;

    @Column(name = DbFieldName.CUSTOM_FIELD_2)
    private String customField2;
}