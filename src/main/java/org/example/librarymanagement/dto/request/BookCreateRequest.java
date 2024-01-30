package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.entity.Genre;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookCreateRequest {
    private MultipartFile picture;

    @NotNull(message = "Title should not be null")
    private String title;

    @NotNull(message = "Description should not be null")
    private String description;

    @Min(value = 0, message = "Quantity smallest value is 0")
    @NotNull(message = "Quantity should not be null")
    private int quantity;

    @NotNull(message = "Genre Id should not be null")
    private Long genreId;

    @NotNull(message = "Author Id should not be null")
    private Long authorId;

    @NotNull(message = "Should state if this is removed or not")
    private boolean removed;
}
