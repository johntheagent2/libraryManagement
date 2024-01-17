package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_generator")
    @SequenceGenerator(name = "session_generator", sequenceName = "session_generator", allocationSize = 1)
    @Column(name = "id")
    private long sessionID;

    @Column(name = "jti", unique = true, nullable = false)
    private String jti;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Session(String jti, Date creationDate, Date expirationDate) {
        this.jti = jti;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
    }
}
