package org.example.librarymanagement.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagement.ExcelSqlHandler;
import org.example.librarymanagement.config.TestConfig;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.AuthenticationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthenticationService authenticationService;

    static final String testFile = "src/test/resources/test/test.xlsx";

    String email = "caophat113@gmail.com";

    String password = "Ducph@t1742002";

    @BeforeEach
    void setUp() throws IOException {
        ExcelSqlHandler.readExcelFile(testFile, "account", 0);
        ExcelSqlHandler.readExcelFile(testFile, "app_user", 1);
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteById(1L);
        appUserRepository.deleteById(2L);
    }


    @Test
    @Order(1)
    void authenticateNoOtp() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(email, password, 0);

        mockMvc.perform(post("/api/v1/common/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.jwtToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andReturn();
    }

    @Test
    @Order(2)
    void refreshTokenWithRefreshToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(email, password, 0);
        AuthenticationResponse response = authenticationService.authenticate(request);

        mockMvc.perform(get("/api/v1/common/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + response.getRefreshToken()))
                .andExpect(jsonPath("$.jwtToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(3)
    void refreshTokenWithJwtToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(email, password, 0);
        AuthenticationResponse response = authenticationService.authenticate(request);

        mockMvc.perform(get("/api/v1/common/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + response.getJwtToken()))
                .andExpect(status().is4xxClientError());
    }
}