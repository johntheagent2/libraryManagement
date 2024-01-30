package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.entity.base.AuditableEntity;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListenerImpl.class)
public class Session extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_generator")
    @SequenceGenerator(name = "session_generator", sequenceName = "session_generator", allocationSize = 1)
    @Column(name = "id")
    private long sessionID;

    @Column(name = "jti", unique = true, nullable = false)
    private String jti;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Session(String jti, Date expirationDate, Account account) {
        this.jti = jti;
        this.expirationDate = expirationDate;
        this.account = account;
    }
}
