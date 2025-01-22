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
public class TransactionData implements Serializable {

    @Column(name = DbFieldName.PAYMENT_REF_NO)
    private String paymentRefNo;

    @Column(name = DbFieldName.AMOUNT)
    private Long amount;

    @Column(name = DbFieldName.CURRENCY)
    private String currency;
}