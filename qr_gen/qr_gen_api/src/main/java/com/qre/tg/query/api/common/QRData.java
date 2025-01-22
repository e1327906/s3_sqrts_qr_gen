package com.qre.tg.query.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import com.qre.tg.entity.ticket.TransactionData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRData implements Serializable {

    @JsonProperty(JsonFieldName.TICKET_TYPE)
    private Integer ticketType;

    @JsonProperty(JsonFieldName.JOURNEY_TYPE)
    private Integer journeyType;

    @JsonProperty(JsonFieldName.SERIAL_NUMBER)
    private String serialNumber;

    @JsonProperty(JsonFieldName.ISSUER_ID)
    private Integer issuerId;

    @JsonProperty(JsonFieldName.GROUP_SIZE)
    private Integer groupSize;

    @JsonProperty(JsonFieldName.PHONE_NO)
    private String phoneNo;

    @JsonProperty(JsonFieldName.EMAIL)
    private String email;

    @JsonProperty(JsonFieldName.EFFECTIVE_DATE_TIME)
    private long effectiveDateTime;

    @JsonProperty(JsonFieldName.OPERATOR_ID)
    private int operatorId;

    @JsonProperty(JsonFieldName.VALIDITY_DOMAIN)
    private int validityDomain;

    @JsonProperty(JsonFieldName.VALIDITY_PERIOD)
    private int validityPeriod;

    @JsonProperty(JsonFieldName.TRANSACTION_DATA)
    private TransactionData transactionData;
}
