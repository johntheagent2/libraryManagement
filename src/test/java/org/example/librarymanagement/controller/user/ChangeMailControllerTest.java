package org.example.librarymanagement.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagement.ExcelSqlHandler;
import org.example.librarymanagement.config.TestConfig;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.request.ChangeEmailRequest;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.repository.TokenOtpRepository;
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
public class ChangeMailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static final String testFile = "src/test/resources/test/test.xlsx";

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenOtpRepository tokenOtpRepository;

    String email = "caophat113@gmail.com";

    String newMail = "caophat_changed@gmail.com";

    String password = "Ducph@t1742002";

    String jwtToken;

    @BeforeEach
    void setUp() throws IOException {
        ExcelSqlHandler.readExcelFile(testFile, "account", 0);
        ExcelSqlHandler.readExcelFile(testFile, "app_user", 1);

        AuthenticationRequest request = new AuthenticationRequest(email, password, 0);
        jwtToken = authenticationService.authenticate(request).getJwtToken();
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteById(1L);
        appUserRepository.deleteById(2L);
    }

    @Test
    void requestChangeMailTestSuccess() throws Exception {
        ChangeEmailRequest request = new ChangeEmailRequest(newMail);

        mockMvc.perform(post("/api/v1/user/change-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @Test
    void changeMailSuccessWithToken() throws Exception {
        TokenOTP tokenOTP;
        String token;
        ChangeEmailRequest request = new ChangeEmailRequest(newMail);

        mockMvc.perform(post("/api/v1/user/change-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
        tokenOTP = tokenOtpRepository.findByAppUser_Email(email);
        token = tokenOTP.getOtpToken();

        mockMvc.perform(put("/api/v1/user/change-mail")
                        .param("token", token)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertFalse(appUserRepository.existsAppUserByEmail(email));
        Assertions.assertEquals(newMail, appUserRepository.findAppUserByEmail(newMail).get().getEmail());
    }
}
