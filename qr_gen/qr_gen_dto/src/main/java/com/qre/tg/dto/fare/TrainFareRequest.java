
package com.qre.tg.dto.fare;

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
public class TrainFareRequest implements Serializable {

    @JsonProperty(JsonFieldName.SRC_STN_ID)
    private Integer srcStnId;

    @JsonProperty(JsonFieldName.DEST_STN_ID)
    private Integer destStnId;

    @JsonProperty(JsonFieldName.TICKET_TYPE)
    private Integer ticketType;

    @JsonProperty(JsonFieldName.JOURNEY_TYPE)
    private Integer journeyType;

    @JsonProperty(JsonFieldName.GROUP_SIZE)
    private Integer groupSize;
}
