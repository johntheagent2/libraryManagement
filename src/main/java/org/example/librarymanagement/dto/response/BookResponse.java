package org.example.librarymanagement.dto.response;

import lombok.*;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.entity.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String picture;
    private String title;
    private String description;
    private int quantity;
    private String genre;
    private String author;
}
