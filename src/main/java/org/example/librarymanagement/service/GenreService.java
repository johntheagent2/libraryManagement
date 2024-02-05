package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.CreateGenreRequest;
import org.example.librarymanagement.entity.Genre;

public interface GenreService {
    void save(CreateGenreRequest request);

    Genre findGenre(Long id);
}
