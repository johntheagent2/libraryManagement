package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.config.audit.AuditingEntityListenerImpl;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "sms_otp")
@EntityListeners({AuditingEntityListenerImpl.class})
public class SmsOtp extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_otp_generator")
    @SequenceGenerator(name = "sms_otp_generator", sequenceName = "sms_otp_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "otp")
    private String otp;

    @Column(name = "current_phone_number")
    private String currentPhoneNumber;

    @Column(name = "new_phone_number")
    private String newPhoneNumber;

    private LocalDateTime expirationDate;

    public SmsOtp(String otp, String currentPhoneNumber, String newPhoneNumber, LocalDateTime expirationDate) {
        this.otp = otp;
        this.currentPhoneNumber = currentPhoneNumber;
        this.newPhoneNumber = newPhoneNumber;
        this.expirationDate = expirationDate;
    }
}
