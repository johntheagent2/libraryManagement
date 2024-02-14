package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "app_user")
public class AppUser extends Account{

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

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<BorrowReceipt> borrowReceipts = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenOTP> otpTokens = new ArrayList<>();

    public AppUser(String firstName, String lastName, String address, String email, String phoneNumber, String password, Role role, AccountStatus status) {
        super(email, phoneNumber, password, role, status);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public void removeTokenOTP(TokenOTP tokenOTP) {
        otpTokens.remove(tokenOTP);
        tokenOTP.setAppUser(null);
    }
}
