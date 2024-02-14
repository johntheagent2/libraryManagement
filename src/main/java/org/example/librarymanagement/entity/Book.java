package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.entity.base.AuditableEntity;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListenerImpl.class)
@Table(name = "book")
public class Book extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
    @SequenceGenerator(name = "book_generator", sequenceName = "book_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Version
    private Long version;

    @Column(name = "picture", nullable = false)
    private String picture;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "removed", nullable = false)
    private Boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(mappedBy = "bookList", fetch = FetchType.LAZY)
    private List<BorrowReceipt> borrowReceipts;

    public Book(String title, String description, int quantity) {
        this.title = title;
        this.description = description;
        this.quantity = quantity;
    }
}
