package com.qre.tg.query.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.entity.user.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register_SuccessfulRegistration_ReturnsAccessTokenAndRefreshToken() throws Exception {

        // given - setup or precondition
        // Given
        UserRequest request = UserRequest.builder()
                .userName("zawminadmin")
                .email("zawminadmin@gmail.com")
                .phoneNumber("1122334455")
                .password("P@ssw0rd")
                .role(RoleType.ROLE_USER.name())
                .build();

        // Convert UserRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        // when - action
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/Register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists());
    }

}