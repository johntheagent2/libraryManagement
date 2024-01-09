package org.example.librarymanagement.model.account.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.model.account.Account;
import org.example.librarymanagement.model.account.Role;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_admin")
public class Admin extends Account {

    public Admin(String email, String phoneNumber, String password, Role role, LocalDateTime creationDate){
        super(email, phoneNumber, password, role, creationDate);
    }
}
