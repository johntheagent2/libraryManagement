package org.example.librarymanagement.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagement.ExcelSqlHandler;
import org.example.librarymanagement.config.TestConfig;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.repository.AdminRepository;
import org.example.librarymanagement.repository.AuthorRepository;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.repository.GenreRepository;
import org.example.librarymanagement.service.AuthenticationService;
import org.example.librarymanagement.service.implement.BookQueryServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManageBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookQueryServiceImpl bookQueryService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AdminRepository adminRepository;

    static final String testFile = "src/test/resources/test/test.xlsx";

    private String email = "admin@example.com";
    private String password = "Ducph@t1742002";

    @BeforeEach
    void setUp() throws IOException {
        ExcelSqlHandler.readExcelFile(testFile, "author", 2);
        ExcelSqlHandler.readExcelFile(testFile, "genre", 3);
        ExcelSqlHandler.readExcelFile(testFile, "account", 6);
        ExcelSqlHandler.readExcelFile(testFile, "app_admin", 5);
    }

    @Test
    @Order(2)
    void getQueriedBooks() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password, 0);
        String jwtToken = authenticationService.authenticate(authenticationRequest).getJwtToken();
        final int numberOfExpectedResult = 3;


        MvcResult result = mockMvc.perform(get("/api/v1/admin/books/search")
                        .header("Authorization", "Bearer " + jwtToken)
                        .queryParam("title.contains", "the"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(content);
        JsonNode contentNode = root.get("content");
        List<BookResponse> books = objectMapper.convertValue(contentNode, new TypeReference<>() {
        });

        Assertions.assertEquals(numberOfExpectedResult, books.size());
    }

    @Test
    @Order(1)
    void addBookWithCSV() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password, 0);
        String jwtToken = authenticationService.authenticate(authenticationRequest).getJwtToken();

        String csvFilePath = "src/test/resources/test/books.csv";
        MockMultipartFile csvFile = new MockMultipartFile("csv",
                "books.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(csvFilePath));

        mockMvc.perform(multipart("/api/v1/admin/books/csv")
                .file(csvFile)
                .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(status().isCreated());
    }

    @Test
    void addBook() throws Exception {
        Long bookId = 2L;
        String addNewBookTitle = "new book title";
        int numberOfExpectedResult = 1;
        String imgPath = "src/test/resources/test/sherlock_holmes.jpg";
        MockMultipartFile img = new MockMultipartFile("file",
                "books.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(imgPath));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password, 0);
        String jwtToken = authenticationService.authenticate(authenticationRequest).getJwtToken();

        mockMvc.perform(multipart("/api/v1/admin/books")
                        .file(img)
                        .param("title", addNewBookTitle)
                        .param("description", "Test Description")
                        .param("quantity", String.valueOf(3))
                        .param("genreId", String.valueOf(1L))
                        .param("authorId", String.valueOf(1L))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful());

        MvcResult result = mockMvc.perform(get("/api/v1/admin/books/search")
                        .header("Authorization", "Bearer " + jwtToken)
                        .queryParam("title.equals", addNewBookTitle))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(content);
        JsonNode contentNode = root.get("content");
        List<BookResponse> books = objectMapper.convertValue(contentNode, new TypeReference<>() {
        });

        Assertions.assertEquals(numberOfExpectedResult, books.size());
    }

    @Test
    void editBook() throws Exception {
        Long bookId = bookRepository.findByTitle("Charlotte's Web").get().getId();
        String imgPath = "src/test/resources/test/sherlock_holmes.jpg";
        String editTitle = "Edit title";
        final int numberOfExpectedResult = 1;

        byte[] imgBytes = Files.readAllBytes(Paths.get(imgPath));
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password, 0);
        String jwtToken = authenticationService.authenticate(authenticationRequest).getJwtToken();

        mockMvc.perform(put("/api/v1/admin/books/{id}", bookId)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(imgBytes)
                        .param("title", editTitle)
                        .param("description", "Test Description")
                        .param("quantity", String.valueOf(3))
                        .param("genreId", String.valueOf(1L))
                        .param("authorId", String.valueOf(1L))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful());

        MvcResult result = mockMvc.perform(get("/api/v1/admin/books/search")
                        .header("Authorization", "Bearer " + jwtToken)
                        .queryParam("title.equals", editTitle))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(content);
        JsonNode contentNode = root.get("content");
        List<BookResponse> books = objectMapper.convertValue(contentNode, new TypeReference<>() {
        });

        Assertions.assertEquals(numberOfExpectedResult, books.size());
    }

    @Test
    void deleteBook() throws Exception {
        String bookTitle = "The Adventures of Sherlock Holmes";
        Long bookId = bookRepository.findByTitle(bookTitle).get().getId();
        final int numberOfExpectedResult = 1;
        MvcResult result;
        String content;
        JsonNode root;
        JsonNode contentNode;

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password, 0);
        String jwtToken = authenticationService.authenticate(authenticationRequest).getJwtToken();

        mockMvc.perform(delete("/api/v1/admin/books/{id}", bookId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        result = mockMvc.perform(get("/api/v1/admin/books/search")
                        .header("Authorization", "Bearer " + jwtToken)
                        .queryParam("removed", "true"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        content = result.getResponse().getContentAsString();

        root = objectMapper.readTree(content);
        contentNode = root.get("content");
        List<BookResponse> books = objectMapper.convertValue(contentNode, new TypeReference<>() {
        });

        Assertions.assertEquals(numberOfExpectedResult, books.size());
    }
}