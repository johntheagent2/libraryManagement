package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.CreateGenreRequest;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.entity.Genre;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.GenreRepository;
import org.example.librarymanagement.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final ResourceBundle resourceBundle;

    @Override
    public void save(CreateGenreRequest request){
        Genre genre = new Genre();
        genre.setName(request.getName());
        genreRepository.save(genre);
    }

    @Override
    public Genre findGenre(Long id){
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        resourceBundle.getString("service.author.not-found"),
                        "service.author.not-found"
                ));
    }
}
