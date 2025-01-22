package com.qre.tg.query.api.common;

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
public class ValidationData implements Serializable {

    @JsonProperty(JsonFieldName.TICKET_DETAIL)
    private QRData QRData;
}
