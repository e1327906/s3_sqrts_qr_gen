package com.qre.tg.dto.qr;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetailResponse implements Serializable {

    @JsonProperty(JsonFieldName.QR_DATA)
    private String qrData;

    @JsonProperty(JsonFieldName.SERIAL_NUMBER)
    private String serialNumber;

    @JsonProperty(JsonFieldName.DEPARTURE_POINT)
    private Integer departurePoint;

    @JsonProperty(JsonFieldName.ARRIVAL_POINT)
    private Integer arrivalPoint;

    @JsonProperty(JsonFieldName.DEPARTURE_POINT_DES)
    private String departurePointDes;

    @JsonProperty(JsonFieldName.ARRIVAL_POINT_DES)
    private String arrivalPointDes;

    @JsonProperty(JsonFieldName.STATUS)
    private Integer status;

    @JsonProperty(JsonFieldName.EFFECTIVE_DATETIME)
    private Long effectiveDatetime;

    @JsonProperty(JsonFieldName.JOURNEY_TYPE)
    private Integer journeyType;

    @JsonProperty(JsonFieldName.JOURNEY_ID)
    private String journeyId;

    @JsonProperty(JsonFieldName.PAYMENT_REF_NO)
    private String paymentRefNo;

    @JsonProperty(JsonFieldName.AMOUNT)
    private Long amount;

}
