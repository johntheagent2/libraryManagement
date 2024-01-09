package org.example.librarymanagement.model.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public class Account{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name = "account_generator", sequenceName = "account_generator", allocationSize = 1)
    @Column(name = "id")
    private Long appUserID;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private Boolean locked = false;
    public Account(String email, String phoneNumber, String password, Role role, LocalDateTime creationDate) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.creationDate = creationDate;
        this.role = role;
        this.password = password;
    }
}
