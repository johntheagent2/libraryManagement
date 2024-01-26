package org.example.librarymanagement.dto.request;

import lombok.Data;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.entity.Genre;

@Data
public class BookCreateRequest {
    private String title;
    private String description;
    private int quantity;
}
