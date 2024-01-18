package org.example.librarymanagement.config.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.example.librarymanagement.entity.AuditableEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

public class AuditingEntityListenerImpl extends AuditingEntityListener {

    @PrePersist
    public void prePersist(Object target) {
        if (target instanceof AuditableEntity auditableEntity) {
            auditableEntity.setCreatedDate(LocalDateTime.now());
            auditableEntity.setLastModifiedDate(LocalDateTime.now());
        }
    }
    @PreUpdate
    public void preUpdate(Object target) {
        if (target instanceof AuditableEntity auditableEntity) {
            auditableEntity.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
