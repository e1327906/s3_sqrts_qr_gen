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
public class TrainRouteResponse implements Serializable {
    @JsonProperty(JsonFieldName.STN_ID)
    private int stnId;
    @JsonProperty(JsonFieldName.STN_CODE)
    private String stnCode;
    @JsonProperty(JsonFieldName.STN_NAME)
    private String stnName;
}

