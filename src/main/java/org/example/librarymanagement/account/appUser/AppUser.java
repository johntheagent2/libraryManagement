package org.example.librarymanagement.account.appUser;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.account.Account;
import org.example.librarymanagement.account.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

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


    public AppUser(String firstName, String lastName, String address, String email, String phoneNumber, String password, LocalDateTime creationDate) {
        super(email, phoneNumber, password, creationDate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
