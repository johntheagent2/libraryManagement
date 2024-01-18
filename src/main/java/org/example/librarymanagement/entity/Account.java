//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example.librarymanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
@EntityListeners({AuditingEntityListenerImpl.class})
public class Account extends AuditableEntity{
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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "count_wrong_login", nullable = false)
    private int countWrongLogin = 0;

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

    public void setAppUserID(final Long appUserID) {
        this.appUserID = appUserID;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public void setStatus(final AccountStatus status) {
        this.status = status;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    public void setCountWrongLogin(final int countWrongLogin) {
        this.countWrongLogin = countWrongLogin;
    }

    public Account() {
    }
}
