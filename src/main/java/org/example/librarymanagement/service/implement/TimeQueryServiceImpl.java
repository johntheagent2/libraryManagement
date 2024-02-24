package org.example.librarymanagement.service.implement;

import org.example.librarymanagement.entity.base.AuditableEntity;
import org.example.librarymanagement.entity.base.AuditableEntity_;
import org.example.librarymanagement.service.criteria.TimeCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.jhipster.service.QueryService;

import java.time.LocalDateTime;

@Service
public class TimeQueryServiceImpl<T> extends QueryService<AuditableEntity> {
    protected Specification<T> createTimeSpecification(TimeCriteria criteria) {
        Specification<T> specification = Specification.where(null);
        if (criteria.getFormatFromTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get(AuditableEntity_.CREATED_DATE)
                            .as(LocalDateTime.class),
                    criteria.getFormatFromTime()));
        }
        if (criteria.getFormatToTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get(AuditableEntity_.CREATED_DATE)
                            .as(LocalDateTime.class),
                    criteria.getFormatToTime()));
        }
        if (criteria.getModifiedFromTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get(AuditableEntity_.LAST_MODIFIED_DATE)
                            .as(LocalDateTime.class),
                    criteria.getModifiedFromTime()));
        }
        if (criteria.getModifiedToTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get(AuditableEntity_.LAST_MODIFIED_DATE)
                            .as(LocalDateTime.class),
                    criteria.getModifiedToTime()));
        }
        return specification;
    }
}
