package org.example.librarymanagement.service.criteria;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.entity.Author;
import org.example.librarymanagement.entity.Genre;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class BookCriteria implements Criteria, Serializable {

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private IntegerFilter quantity;

    private LongFilter genreId;

    private LongFilter authorId;

    private LocalDateFilter createdDate;

    private LocalDateFilter lastModifiedDate;

    private Boolean distinct;

    public BookCriteria(BookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.genreId = other.genreId == null ? null : other.genreId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }


    @Override
    public Criteria copy() {
        return new BookCriteria(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BookCriteria that = (BookCriteria) object;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title)
                && Objects.equals(description, that.description) && Objects.equals(quantity, that.quantity)
                && Objects.equals(genreId, that.genreId) && Objects.equals(authorId, that.authorId)
                && Objects.equals(createdDate, that.createdDate) && Objects.equals(lastModifiedDate, that.lastModifiedDate)
                && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description,
                quantity, genreId, authorId,
                createdDate, lastModifiedDate, distinct);
    }

    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
}