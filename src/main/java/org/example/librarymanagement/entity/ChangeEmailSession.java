package org.example.librarymanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.entity.base.ChangeResetSession;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "change_email")
public class ChangeEmailSession extends ChangeResetSession {

    @Column(name = "current_email", nullable = false)
    private String currentEmail;

    @Column(name = "new_email", nullable = false)
    private String newEmail;

    public ChangeEmailSession(String token, String currentEmail, String newEmail, LocalDateTime expirationDate, AppUser appUser) {
        super(token, expirationDate, appUser);
        this.currentEmail = currentEmail;
        this.newEmail = newEmail;
    }
}
