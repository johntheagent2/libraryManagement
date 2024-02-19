package org.example.librarymanagement.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagement.ExcelSqlHandler;
import org.example.librarymanagement.config.TestConfig;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.request.BorrowBookRequest;
import org.example.librarymanagement.dto.response.BookResponse;
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
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BorrowBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;

    static final String testFile = "src/test/resources/test/test.xlsx";

    String email = "caophat113@gmail.com";

    String password = "Ducph@t1742002";

    String jwtToken;

    @BeforeEach
    void setUp() throws IOException {
        ExcelSqlHandler.readExcelFile(testFile, "author", 2);
        ExcelSqlHandler.readExcelFile(testFile, "genre", 3);
        ExcelSqlHandler.readExcelFile(testFile, "book", 4);
        ExcelSqlHandler.readExcelFile(testFile, "account", 0);
        ExcelSqlHandler.readExcelFile(testFile, "app_user", 1);

        AuthenticationRequest request = new AuthenticationRequest(email, password, 0);
        jwtToken = authenticationService.authenticate(request).getJwtToken();
    }

    @Test
    @Order(1)
    void borrowBook() throws Exception {

        Long idBook1 = 1L;
        Long idBook2 = 4L;

        // Create sample BorrowBookRequest objects
        String requestBody = toJson(idBook1, idBook2);

        mockMvc.perform(post("/api/v1/user/books")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void getCurrentBorrowedBook() throws Exception {
        int numberOfExpectedResult = 2;
        String content;
        List<BookResponse> books;

        MvcResult result = mockMvc.perform(get("/api/v1/user/books/current")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        content = result.getResponse().getContentAsString();

        books = objectMapper.readValue(
                content,
                new TypeReference<>() {
                }
        );

        Assertions.assertEquals(numberOfExpectedResult, books.size());
    }

    @Test
    @Order(3)
    void returnBorrowedBook() throws Exception {

        Long idBook1 = 1L;
        Long idBook2 = 4L;

        // Create sample BorrowBookRequest objects
        String requestBody = toJson(idBook1, idBook2);

        mockMvc.perform(post("/api/v1/user/books/return")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    private static String toJson(Long idBook1, Long idBook2) throws JsonProcessingException {
        BorrowBookRequest request1 = new BorrowBookRequest(idBook1);
        BorrowBookRequest request2 = new BorrowBookRequest(idBook2);

        // Add them to a list
        List<BorrowBookRequest> requestList = new ArrayList<>();
        requestList.add(request1);
        requestList.add(request2);

        // Convert the requestList to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestList);
        return requestBody;
    }
}