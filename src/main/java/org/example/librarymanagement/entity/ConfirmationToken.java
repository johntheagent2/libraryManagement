package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token, String otp, LocalDateTime localDateTime, LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.otp = otp;
        this.localDateTime = localDateTime;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}

