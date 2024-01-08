package org.example.librarymanagement.registration.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.account.appUser.AppUser;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "token_id_seq")
    @SequenceGenerator(
            name = "token_id_seq",
            sequenceName = "global_id_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String token;

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

    public ConfirmationToken(String token, LocalDateTime localDateTime, LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.localDateTime = localDateTime;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}
