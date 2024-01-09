package org.example.librarymanagement.model.account.appUser;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.model.account.Account;
import org.example.librarymanagement.model.account.Role;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_user")
public class AppUser extends Account {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    private Boolean enabled = false;


    public AppUser(String firstName, String lastName, String address, String email, String phoneNumber, String password, Role role, LocalDateTime creationDate) {
        super(email, phoneNumber, password, role, creationDate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
