
package com.qre.tg.dto.fare;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FareRequest implements Serializable {


    private Integer startPoint;

    private Integer endPoint;

    private Integer ticketType;

    private Integer journeyType;

    private Integer groupSize;
}
