package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.entity.Author_;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.entity.Book_;
import org.example.librarymanagement.entity.Genre_;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.service.criteria.BookCriteria;
import org.example.librarymanagement.service.criteria.TimeCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@AllArgsConstructor
@Service
public class BookQueryServiceImpl extends QueryService<Book> {

    private final BookRepository bookRepository;
    private final TimeQueryServiceImpl<Book> timeQueryService;

    @Transactional(readOnly = true)
    public Page<BookResponse> getAvailableBook(Pageable page) {
        BookCriteria criteria = new BookCriteria();
        criteria.setRemoved(false);
        return getBookResponses(criteria, page);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findByCriteria(BookCriteria criteria,
                                             TimeCriteria createdDate,
                                             TimeCriteria lastModifiedDate,
                                             Pageable page) {
        return getBookResponses(criteria, createdDate, lastModifiedDate, page);
    }

    private Page<BookResponse> getBookResponses(BookCriteria criteria, Pageable page) {
        final Specification<Book> specification = createSpecification(criteria);
        return toResponse(page, specification);
    }


    private Page<BookResponse> getBookResponses(BookCriteria criteria,
                                                TimeCriteria createdDate,
                                                TimeCriteria lastModifiedDate,
                                                Pageable page) {
        Specification<Book> specification = createSpecification(criteria)
                .and(timeQueryService.createCreatedDateSpecification(createdDate))
                .and(timeQueryService.createLastModifiedDateSpecification(lastModifiedDate));

        return toResponse(page, specification);
    }


    private Page<BookResponse> toResponse(Pageable page, Specification<Book> specification) {
        Page<Book> books = bookRepository.findAll(specification, page);
        return books.map(book -> BookResponse.builder()
                .id(book.getId())
                .picture(book.getPicture())
                .title(book.getTitle())
                .description(book.getDescription())
                .quantity(book.getQuantity())
                .genre(book.getGenre().getName())
                .author(book.getAuthor().getName())
                .build());
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
            if (criteria.getDescription() != null) {
                specification = specification.and(buildSpecification(criteria.getDescription(), Book_.description));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Book_.quantity));
            }
            if (criteria.getGenreId() != null) {
                specification = specification.and(buildSpecification(criteria.getGenreId(), root -> root.join(Book_.genre).get(Genre_.id)));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthorId(), root -> root.join(Book_.author).get(Author_.id)));
            }
            if (criteria.getRemoved() != null) {
                specification = specification.and((root, query, builder) ->
                        builder.equal(root.get(Book_.removed), criteria.getRemoved()));
            }
        }
        return specification;
    }
}
