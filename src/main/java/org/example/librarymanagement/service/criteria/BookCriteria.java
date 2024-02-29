package org.example.librarymanagement.service.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
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

    private Boolean removed;

    private Boolean distinct;

    public BookCriteria(BookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.genreId = other.genreId == null ? null : other.genreId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.removed = other.removed;
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
                && Objects.equals(removed, that.removed)
                && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description,
                quantity, genreId, authorId, removed, distinct);
    }
}