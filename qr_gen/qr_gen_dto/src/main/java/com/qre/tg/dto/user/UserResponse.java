package com.qre.tg.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {


    @JsonProperty(JsonFieldName.USER_ID)
    private String userId;

    @JsonProperty(JsonFieldName.USER_NAME)
    private String userName;

    @JsonProperty(JsonFieldName.PHONE_NUMBER)
    private String phoneNumber;

    @JsonProperty(JsonFieldName.EMAIL)
    private String email;

    @JsonProperty(JsonFieldName.PASSWORD)
    private String password;
}
