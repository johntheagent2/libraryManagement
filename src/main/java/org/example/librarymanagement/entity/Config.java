package org.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.librarymanagement.enumeration.ConfigType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "redis_generator")
    @SequenceGenerator(name = "redis_generator", sequenceName = "redis_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "key", nullable = false)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ConfigType type;

    @Column(name = "value", nullable = false)
    private String value;
}
