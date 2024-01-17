package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_user")
public class AppUser extends Account {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "mfa", nullable = false)
    private Boolean mfa = false;

    @Column(name = "secret_key")
    private String secretKey;

    public AppUser(String firstName, String lastName, String address, String email, String phoneNumber, String password, Role role, AccountStatus status, LocalDateTime creationDate) {
        super(email, phoneNumber, password, role, status, creationDate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
