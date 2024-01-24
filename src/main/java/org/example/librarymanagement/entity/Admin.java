package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "app_admin")
public class Admin extends Account {

    @Column(name = "full_name")
    private String fullName;

    public Admin(String email, String phoneNumber, String password, Role role, AccountStatus status){
        super(email, phoneNumber, password, role, status);
    }
}
