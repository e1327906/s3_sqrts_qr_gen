package com.qre.tg.dto.route;

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
public class BusRouteResponse implements Serializable {

    @JsonProperty(JsonFieldName.BUS_STOP_ID)
    private int busStopId;

    @JsonProperty(JsonFieldName.BUS_STOP_CODE)
    private String busStopCode;

    @JsonProperty(JsonFieldName.BUS_STOP_FULL_NAME)
    private String busStopFullName;
}
