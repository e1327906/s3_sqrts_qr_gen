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
public class TicketUpdateStatusRequest implements Serializable {

    @JsonProperty(JsonFieldName.SERIAL_NUMBER)
    private String serialNumber;

    @JsonProperty(JsonFieldName.STATUS)
    private Integer status;

    @JsonProperty(JsonFieldName.ENTRY_DATE_TIME)
    private long entryDateTime;

    @JsonProperty(JsonFieldName.EXIT_DATE_TIME)
    private long exitDateTime;

}
