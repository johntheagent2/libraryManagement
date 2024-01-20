package org.example.librarymanagement.entity;

import jakarta.persistence.*;
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
@Table(name = "reset_password")
public class ResetPasswordSession extends ChangeResetSession {

    @Column(name = "new_password", nullable = false)
    private String newPassword;

    public ResetPasswordSession(String token, String newPassword, LocalDateTime expirationDate, AppUser appUser) {
        super(token, expirationDate, appUser);
        this.newPassword = newPassword;
    }
}
