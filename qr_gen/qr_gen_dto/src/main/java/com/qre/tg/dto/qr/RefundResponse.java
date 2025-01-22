package com.qre.tg.dto.qr;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundResponse {

    @JsonProperty(JsonFieldName.SERIAL_NUMBER)
    private String serialNumber;

    @JsonProperty(JsonFieldName.PAYMENT_INTENT_ID)
    private String paymentIntentId;

    @JsonProperty(JsonFieldName.REFUND_AMOUNT)
    private long refundAmount;

    @JsonProperty(JsonFieldName.REFUND_STATUS)
    private int refundStatus; //0 in_progress, 1 approved and 3 reject

}
