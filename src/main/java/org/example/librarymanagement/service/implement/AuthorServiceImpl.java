package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl {

    private final AuthorRepository authorRepository;

    public void save(Author author){
        authorRepository.save(author);
    }

    public List<Author> getAllAuthor(){
        return authorRepository.findAll();
    }
}
