package org.example.librarymanagement.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.entity.AppUser;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListenerImpl.class})
public class RequestBaseEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_pass_seq")
    @SequenceGenerator(name = "reset_pass_seq", sequenceName = "reset_pass_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    public RequestBaseEntity(String token, LocalDateTime expirationDate, AppUser appUser) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.appUser = appUser;
    }
}
