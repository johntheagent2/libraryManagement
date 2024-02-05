package org.example.librarymanagement.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagement.config.TestConfig;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.dto.request.RegistrationRequest;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.repository.AccountRepository;
import org.example.librarymanagement.repository.ConfirmationTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    private String email = "caophat@gmail.com";

    @BeforeEach
    void setUp() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("Phat")
                .lastName("Cao")
                .address("Address 1")
                .email(email)
                .phoneNumber("0707854816")
                .password("Ducph@t1742002")
                .build();

        // Perform registration to generate tokens
        mockMvc.perform(post("/api/v1/common/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());

    }

    @AfterEach
    void tearDown() {
        Optional<ConfirmationToken> token = confirmationTokenRepository
                .findConfirmationTokenByAppUser_Email(email);
        if (token.isPresent()) {
            confirmationTokenRepository.deleteById(confirmationTokenRepository
                    .findConfirmationTokenByAppUser_Email(email).get().getId());
        }
        accountRepository.deleteById(accountRepository
                .findAccountByEmail(email).get().getId());
    }

    @Test
    void confirmToken() throws Exception {
        String confirmationToken = confirmationTokenRepository
                .findConfirmationTokenByAppUser_Email(email).get().getToken();

        mockMvc.perform(put("/api/v1/common/registration/confirm")
                        .param("token", confirmationToken))
                .andExpect(status().is2xxSuccessful());

        Account account = accountRepository.findAccountByEmail(email).get();
        assertEquals(AccountStatus.ACTIVE.name(), account.getStatus().name());
    }

    @Test
    void confirmOtpToken() throws Exception {
        String confirmationOTP = confirmationTokenRepository
                .findConfirmationTokenByAppUser_Email(email).get().getOtp();

        OtpVerificationRequest verificationRequest = new OtpVerificationRequest(confirmationOTP);

        mockMvc.perform(put("/api/v1/common/registration/confirm-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationRequest)))
                .andExpect(status().is2xxSuccessful());

        Account account = accountRepository.findAccountByEmail(email).get();
        assertEquals(AccountStatus.ACTIVE.name(), account.getStatus().name());
    }

    @Test
    void resendToken() throws Exception {
        mockMvc.perform(put("/api/v1/common/registration/resend")
                        .param("email", email))
                .andExpect(status().is2xxSuccessful());
    }
}