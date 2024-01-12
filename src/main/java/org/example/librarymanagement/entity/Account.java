package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.enumeration.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
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

    public UserDetails toUserDetails(){
        return new CustomUserDetails(this);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
