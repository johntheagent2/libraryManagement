package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;
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


    public AppUser(String firstName, String lastName, String address, String email, String phoneNumber, String password, Role role, LocalDateTime creationDate) {
        super(email, phoneNumber, password, role, creationDate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
