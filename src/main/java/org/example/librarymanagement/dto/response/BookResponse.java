package org.example.librarymanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String description;
    private int quantity;
    private Long genreId;
    private Long authorId;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}
