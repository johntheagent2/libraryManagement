package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.CreateAuthorRequest;
import org.example.librarymanagement.entity.Author;

public interface AuthorService {
    void save(CreateAuthorRequest request);

    Author findAuthor(Long id);
}
