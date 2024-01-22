package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.entity.base.RequestBaseEntity;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "change_phone_number")
public class ChangePhoneNumberRequest extends RequestBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_otp_generator")
    @SequenceGenerator(name = "sms_otp_generator", sequenceName = "sms_otp_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "current_phone_number", nullable = false)
    private String currentPhoneNumber;

    @Column(name = "new_phone_number", nullable = false)
    private String newPhoneNumber;

    public ChangePhoneNumberRequest(String otp, String currentPhoneNumber, String newPhoneNumber, LocalDateTime expirationDate, AppUser appUser) {
        super(otp, expirationDate, appUser);
        this.currentPhoneNumber = currentPhoneNumber;
        this.newPhoneNumber = newPhoneNumber;
    }
}
