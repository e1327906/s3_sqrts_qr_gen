package com.qre.tg.dto.qr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseTicketRequest implements Serializable {

    //@JsonProperty(JsonFieldName.ISSUER_ID)
    @JsonIgnore
    private Integer issuerId = 10;

    //@JsonProperty(JsonFieldName.APP_TYPE)
    @JsonIgnore
    private String appType = "WEB";

    //@JsonProperty(JsonFieldName.TICKET_TYPE)
    @JsonIgnore
    private Integer ticketType=1; //journey

    @JsonProperty(JsonFieldName.JOURNEY_TYPE)
    private Integer journeyType=1;

    @JsonProperty(JsonFieldName.GROUP_SIZE)
    private Integer groupSize = 0;

    //@JsonProperty(JsonFieldName.VALIDITY_DOMAIN)
    @JsonIgnore
    private Integer validityDomain = 1; //train/bus/tram/all

    @JsonProperty(JsonFieldName.OPERATOR_ID)
    private Integer operatorId = 3;

    //@JsonProperty(JsonFieldName.CREATION_DATETIME)
    @JsonIgnore
    private Long creationDatetime = new Date().getTime();

    @JsonProperty(JsonFieldName.START_DATETIME)
    private Long startDatetime  = new Date().getTime();;

    @JsonProperty(JsonFieldName.END_DATETIME)
    private Long endDatetime;

    @JsonProperty(JsonFieldName.DEPARTURE_POINT)
    private Integer departurePoint;

    @JsonProperty(JsonFieldName.ARRIVAL_POINT)
    private Integer arrivalPoint;

    @JsonProperty(JsonFieldName.PAYMENT_REF_NO)
    private String paymentRefNo;

    @JsonProperty(JsonFieldName.AMOUNT)
    private Long amount;

    @JsonProperty(JsonFieldName.CURRENCY)
    private String currency;

    @JsonProperty(JsonFieldName.PHONE_NO)
    private String phoneNo;

    @JsonProperty(JsonFieldName.EMAIL)
    private String email;

}
