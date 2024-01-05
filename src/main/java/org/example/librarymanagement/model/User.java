package org.example.librarymanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String email;

    private String password;

    private String address;

    private String birthday;

    @Column(name = "is_enabled")
    private Boolean isEnabled;


}
