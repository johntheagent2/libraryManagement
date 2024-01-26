package org.example.librarymanagement.service.implement;

import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.entity.Author_;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.entity.Book_;
import org.example.librarymanagement.entity.Genre_;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.service.criteria.BookCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class BookQueryServiceImpl extends QueryService<Book> {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BookResponse> findByCriteria(BookCriteria criteria, Pageable page) {
        final Specification<Book> specification = createSpecification(criteria);
        Page<Book> books = bookRepository.findAll(specification, page);

        List<BookResponse> bookResponses = books.getContent().stream()
                .map(book -> BookResponse.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .quantity(book.getQuantity())
                        .genreId(book.getGenre().getId())
                        .authorId(book.getAuthor().getId())
                        .createdDate(LocalDate.from(book.getCreatedDate()))
                        .lastModifiedDate(LocalDate.from(book.getLastModifiedDate()))
                        .build())
                .toList();

        // Map Book to BookResponse
        return new PageImpl<>(bookResponses, page, books.getTotalElements());
    }

    private Specification<Book> createSpecification(BookCriteria criteria) {
        Specification<Book> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Book_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Book_.title));
            }
            if(criteria.getDescription() != null) {
                specification = specification.and(buildSpecification(criteria.getDescription(), Book_.description));
            }
            if(criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Book_.quantity));
            }
            if(criteria.getGenreId() != null) {
                specification = specification.and(buildSpecification(criteria.getGenreId(), root -> root.join(Book_.genre).get(Genre_.id)));
            }
            if(criteria.getAuthorId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthorId(), root -> root.join(Book_.author).get(Author_.id)));
            }
            if(criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Book_.createdDate));
            }
            if(criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Book_.lastModifiedDate));
            }
        }
        return specification;
    }
}
