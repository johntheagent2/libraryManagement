package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_generator")
    @SequenceGenerator(name = "session_generator", sequenceName = "session_generator", allocationSize = 1)
    @Column(name = "id")
    private long sessionID;

    @Column(name = "jti", unique = true, nullable = false)
    private String jti;

    @Column(name = "creation_date", nullable = false)
    private String creationDate;

    @Column(name = "update_date", nullable = false)
    private String updateDate;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
