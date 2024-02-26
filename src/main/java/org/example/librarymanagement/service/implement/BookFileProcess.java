package org.example.librarymanagement.service.implement;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BookCreateRequest;
import org.example.librarymanagement.exception.exception.ApiRequestException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BookFileProcess {

    //    @Value("${img.upload.dir}")
    private static String uploadDir = "src/main/resources/images/bookcover";

    @Value("${img.default.book-cover}")
    private String defaultBookCover;

    public static List<BookCreateRequest> addCSV(MultipartFile file, ResourceBundle resourceBundle) {
        String[] line;
        String title;
        String description;
        String priceStr;
        String genreIdStr;
        String quantityStr;
        String authorIdStr;
        int quantity;
        double price;
        long genreId;
        long authorId;
        List<BookCreateRequest> bookList = new ArrayList<>();

        validateCSV(file, resourceBundle);

        try (
                Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVReader csvReader = new CSVReader(reader)
        ) {
            String[] header = csvReader.readNext();

            if (!isValidHeader(header)) {
                throw new IllegalArgumentException("Invalid CSV header");
            }

            while ((line = csvReader.readNext()) != null) {
                title = line[0];
                description = line[1];
                priceStr = line[2];
                quantityStr = line[3];
                genreIdStr = line[4];
                authorIdStr = line[5];

                validateAttributes(title, description, quantityStr, genreIdStr, authorIdStr, resourceBundle);

                price = Double.parseDouble(priceStr);
                quantity = Integer.parseInt(quantityStr);
                genreId = Long.parseLong(genreIdStr);
                authorId = Long.parseLong(authorIdStr);

                BookCreateRequest book = new BookCreateRequest();
                book.setTitle(title);
                book.setDescription(description);
                book.setQuantity(quantity);
                book.setPrice(BigDecimal.valueOf(price));
                book.setGenreId(genreId);
                book.setAuthorId(authorId);
                bookList.add(book);
            }
        } catch (IOException | CsvValidationException e) {
            throw new ApiRequestException(e.getMessage());
        }
        return bookList;
    }

    private static boolean isValidHeader(String[] header) {
        String[] requiredFields = {"title", "description", "price", "quantity", "genreId", "authorId"};

        if (header == null || header.length != 6) {
            return false;
        }
        for (String field : requiredFields) {
            boolean found = false;
            for (String columnHeader : header) {
                if (columnHeader.trim().equalsIgnoreCase(field)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }


    public static void validateCSV(MultipartFile file, ResourceBundle resourceBundle) {
        long maxSize = 5 * 1024 * 1024;

        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            throw new BadRequestException("opencsv.csv.invalid",
                    resourceBundle.getString("opencsv.csv.invalid"));
        }

        if (file.getSize() > maxSize) {
            throw new BadRequestException("service.fields.null",
                    resourceBundle.getString("service.fields.null"));
        }
    }

    public static void validateAttributes(String title, String description, String genreIdStr, String quantityStr, String authorIdStr, ResourceBundle resourceBundle) {
        if (title.isEmpty() || description.isEmpty() || quantityStr.isEmpty() || genreIdStr.isEmpty() || authorIdStr.isEmpty()) {
            throw new BadRequestException("service.fields.null",
                    resourceBundle.getString("service.fields.null"));
        }

        if (Integer.parseInt(quantityStr) <= 0) {
            throw new BadRequestException("service.quantity.not-found",
                    resourceBundle.getString("service.quantity.not-found"));
        }
    }

    public static void deleteFile(String dir) {
        // Create a Path object
        Path path = Paths.get(dir);

        // Attempt to delete the file
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException("util.io.deletion",
                    "util.io.deletion");
        }
    }

    public static String saveUploadedFile(MultipartFile file, ResourceBundle resourceBundle) throws IOException {
        if (file == null || file.isEmpty()) {
            return "src/main/resources/images/bookcover/default_book_cover.jpg";
        } else {
            return modifiedFile(file, resourceBundle);
        }
    }

    public static String modifiedFile(MultipartFile file, ResourceBundle resourceBundle) {
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String modifiedFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));

        validateImgFile(originalFileName);

        Path uploadPath = Paths.get(uploadDir);

        try {
            Files.copy(file.getInputStream(), uploadPath.resolve(modifiedFileName));
            return uploadDir + "/" + modifiedFileName;
        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException(
                    resourceBundle.getString("util.io.exception"),
                    "util.io.exception"
            );
        }
    }

    public static void validateImgFile(String originalFileName) {
        System.out.println(originalFileName);
        if (originalFileName.endsWith(".jpg") || originalFileName.endsWith(".jpeg") || originalFileName.endsWith(".png")) {
            return;
        }
        throw new ApiRequestException("Invalid Image File");
    }
}
