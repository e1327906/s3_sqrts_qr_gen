package com.qre.tg.dto.user;

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
public class OtpRequest implements Serializable {

    @JsonProperty(JsonFieldName.USER_NAME)
    private String userName; //username will be email or phone

    @JsonProperty(value = JsonFieldName.EMAIL_OR_PHONE)
    @Builder.Default
    private boolean emailOrPhone = true;

    @JsonProperty(value = JsonFieldName.OTP_NUM)
    private Integer otpNum;
}
