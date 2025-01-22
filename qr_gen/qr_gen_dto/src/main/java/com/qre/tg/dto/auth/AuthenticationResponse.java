package com.qre.tg.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qre.tg.dto.base.JsonFieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty(JsonFieldName.ACCESS_TOKEN)
  private String accessToken;

  @JsonProperty(JsonFieldName.REFRESH_TOKEN)
  private String refreshToken;

  @JsonProperty(JsonFieldName.USER_NAME)
  private String userName;

  @JsonProperty(JsonFieldName.EMAIL)
  private String email;

  @JsonProperty(JsonFieldName.ROLE)
  private String role;

  @JsonProperty(JsonFieldName.PHONE_NUMBER)
  private String phoneNumber;

  @JsonProperty(JsonFieldName.USER_ID)
  private String userId;
}
