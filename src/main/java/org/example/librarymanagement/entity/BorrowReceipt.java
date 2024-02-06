package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.entity.base.AuditableEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListenerImpl.class)
@Table(name = "borrow_receipt")
public class BorrowReceipt extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receipt_generator")
    @SequenceGenerator(name = "receipt_generator", sequenceName = "receipt_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_price", nullable = false)
    private double totalPrice = 0;

    @Column(name = "active")
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_receipt",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "receipt_id")
    )
    private List<Book> bookList;

    public BorrowReceipt(List<Book> bookList, AppUser appUser){
        if(!bookList.isEmpty()){
            this.totalPrice = bookList.stream()
                    .mapToDouble(Book::getPrice).sum();
        }
        this.appUser = appUser;
        this.bookList = bookList;
    }
}
