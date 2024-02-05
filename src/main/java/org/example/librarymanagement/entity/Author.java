package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")
    @SequenceGenerator(name = "author_generator", sequenceName = "author_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "birth_day", nullable = false)
    private LocalDate birthDay;

    @Column(name = "biography", nullable = false)
    private String biography;

    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Book> books;
}
