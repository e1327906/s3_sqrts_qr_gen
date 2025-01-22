package com.qre.tg.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseRequest implements Serializable {

    @JsonProperty(JsonFieldName.TOKEN)
    private String token;

    @JsonProperty(JsonFieldName.TOKEN_TYPE)
    private String tokenType = "Bearer";

    @JsonProperty(JsonFieldName.USER_ID)
    private String userId;

}
