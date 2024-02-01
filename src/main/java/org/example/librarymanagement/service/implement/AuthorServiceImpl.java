package org.example.librarymanagement.service.implement;


import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.CreateAuthorRequest;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.AuthorRepository;
import org.example.librarymanagement.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ResourceBundle resourceBundle;

    @Override
    public void save(CreateAuthorRequest request){
        Author author = Author.builder()
                .name(request.getName())
                .nationality(request.getNationality())
                .birthDay(request.getBirthDay())
                .biography(request.getBiography())
                .build();

        authorRepository.save(author);
    }

    @Override
    public Author findAuthor(Long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        resourceBundle.getString("service.author.not-found"),
                        "service.author.not-found"
                ));
    }

}
