package com.qre.tg.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.HttpRetryException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class APIResponse {
    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseMsg")
    private String responseMsg;

    @JsonProperty("ResponseData")
    private Object responseData;

}

