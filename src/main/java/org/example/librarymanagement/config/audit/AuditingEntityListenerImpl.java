package org.example.librarymanagement.config.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.base.AuditableEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class AuditingEntityListenerImpl extends AuditingEntityListener {

    private final AuditorAwareImpl auditorAware;

    @PrePersist
    public void prePersist(Object target) {
        if (target instanceof AuditableEntity auditableEntity) {
            auditableEntity.setCreatedDate(LocalDateTime.now());
            auditableEntity.setCreatedBy(auditorAware.getCurrentAuditor().get());
        }
    }

    @PreUpdate
    public void preUpdate(Object target) {
        if (target instanceof AuditableEntity auditableEntity) {
            auditableEntity.setLastModifiedDate(LocalDateTime.now());
            auditableEntity.setLastModifiedBy(auditorAware.getCurrentAuditor().get());
        }
    }
}
