package org.example.librarymanagement.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagement.ExcelSqlHandler;
import org.example.librarymanagement.config.TestConfig;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.repository.TokenOtpRepository;
import org.example.librarymanagement.service.AppUserService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResetPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenOtpRepository tokenOtpRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AuthenticationService authenticationService;

    static final String testFile = "src/test/resources/test/test.xlsx";

    String email = "caophat113@gmail.com";

    String newPassword = "Ducph@t69481";

    String oldPassword = "Ducph@1742002";

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
    void requestResetPassword() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest(email, newPassword);

        mockMvc.perform(post("/api/v1/common/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void confirmResetPasswordAndTryLoginWithOldPassword() throws Exception {
        String token;
        AuthenticationRequest authenticationRequest;
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(email, newPassword);

        appUserService.requestResetPassword(resetPasswordRequest);
        TokenOTP tokenOTP = tokenOtpRepository.findByAppUser_Email(email);
        token = tokenOTP.getOtpToken();

        mockMvc.perform(put("/api/v1/common/reset-password")
                        .param("token", token)
                        .param("email", email))
                .andExpect(status().is2xxSuccessful());

        authenticationRequest = new AuthenticationRequest(email, oldPassword, 0);
        mockMvc.perform(post("/api/v1/common/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().is4xxClientError());
    }
}