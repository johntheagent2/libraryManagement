package org.example.librarymanagement.account;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public class Account{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "account_id_seq")
    @SequenceGenerator(
            name = "account_id_seq",
            sequenceName = "global_id_sequence",
            allocationSize = 1
    )
    @Column(name = "app_user_id")
    private Long appUserID;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private Boolean locked = false;
    public Account(String email, String phoneNumber, String password, LocalDateTime creationDate) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.creationDate = creationDate;
        this.password = password;
    }
}
