package org.example.librarymanagement.account.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.account.Account;
import org.example.librarymanagement.account.Role;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_admin")
public class Admin extends Account{

    @Enumerated(EnumType.STRING)
    private Role role;

    public Admin(String email, String phoneNumber, String password, LocalDateTime creationDate, Role role){
        super(email, phoneNumber, password, creationDate);
        this.role = role;
    }
}
