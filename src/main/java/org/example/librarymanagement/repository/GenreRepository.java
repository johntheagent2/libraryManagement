package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
