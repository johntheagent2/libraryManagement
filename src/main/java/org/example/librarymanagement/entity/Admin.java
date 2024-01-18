package org.example.librarymanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_admin")
public class Admin extends Account {

    public Admin(String email, String phoneNumber, String password, Role role, AccountStatus status){
        super(email, phoneNumber, password, role, status);
    }
}
