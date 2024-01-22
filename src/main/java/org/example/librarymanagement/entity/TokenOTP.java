package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.entity.base.AuditableEntity;
import org.example.librarymanagement.enumeration.ChangeType;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListenerImpl.class)
@Table(name = "otp_token")
public class TokenOTP extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_token_seq")
    @SequenceGenerator(name = "otp_token_seq", sequenceName = "otp_token_seq", allocationSize = 1)
    @Column(name = "id")
    public Long id;

    @Column(name = "otp_token", nullable = false)
    public String otpToken;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public ChangeType type;

    @Column(name = "request", nullable = false)
    public String request;

    @Column(name = "expiration_date", nullable = false)
    public LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public TokenOTP(String otpToken, String request, ChangeType type, LocalDateTime expirationDate, AppUser appUser) {
        this.otpToken = otpToken;
        this.request = request;
        this.type = type;
        this.expirationDate = expirationDate;
        this.appUser = appUser;
    }
}
