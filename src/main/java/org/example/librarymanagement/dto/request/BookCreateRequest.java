package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateRequest {
    private MultipartFile picture;

    @NotNull(message = "Title should not be null")
    private String title;

    @NotNull(message = "Description should not be null")
    private String description;

    @Min(value = 0, message = "Quantity smallest value is 0")
    @NotNull(message = "Quantity should not be null")
    private int quantity;

    @NotNull(message = "Price should not be null")
    @Min(value = 0, message = "Price smallest value is 0")
    @Max(value = 99999, message = "Price to high")
    private BigDecimal price;

    @NotNull(message = "Genre Id should not be null")
    private Long genreId;

    @NotNull(message = "Author Id should not be null")
    private Long authorId;

    @NotNull(message = "Should state if this is removed or not")
    private boolean removed;
}
