//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example.librarymanagement.entity.base;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.entity.CustomUserDetails;
import org.example.librarymanagement.entity.Session;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name = "account_generator", sequenceName = "account_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "count_wrong_login", nullable = false)
    private int countWrongLogin = 0;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessionList = new ArrayList<>();

    public Account(String email, String phoneNumber, String password, Role role, AccountStatus status) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    public UserDetails toUserDetails(){
        return new CustomUserDetails(this);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void wrongLoginCount(){
        ++this.countWrongLogin;
    }

    public Account() {
    }
}
