package org.example.librarymanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Long id;
    private String picture;
    private String title;
    private String description;
    private String genre;
    private String author;
}
