package org.example.librarymanagement.service.implement;

import org.example.librarymanagement.entity.base.AuditableEntity;
import org.example.librarymanagement.entity.base.AuditableEntity_;
import org.example.librarymanagement.service.criteria.ModifiedTimeCriteria;
import org.example.librarymanagement.service.criteria.TimeCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.jhipster.service.QueryService;

import java.time.LocalDateTime;

@Service
public class TimeQueryServiceImpl<T> extends QueryService<AuditableEntity> {
    protected Specification<T> createTimeSpecification(TimeCriteria criteria) {
        Specification<T> specification = Specification.where(null);
        if (criteria.getCreatedDateFromTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get(AuditableEntity_.CREATED_DATE)
                            .as(LocalDateTime.class),
                    criteria.getFormatFromTime()));
        }
        if (criteria.getCreatedDateToTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get(AuditableEntity_.CREATED_DATE)
                            .as(LocalDateTime.class),
                    criteria.getFormatToTime()));
        }
        return specification;
    }

    protected Specification<T> modifiedTimeSpecification(ModifiedTimeCriteria modifiedTimeCriteria) {
        Specification<T> specification = Specification.where(null);
        if (modifiedTimeCriteria.getModifiedFromTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get(AuditableEntity_.LAST_MODIFIED_DATE)
                            .as(LocalDateTime.class),
                    modifiedTimeCriteria.getFormatFromTime()));
        }
        if (modifiedTimeCriteria.getModifiedToTime() != null) {
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get(AuditableEntity_.LAST_MODIFIED_DATE)
                            .as(LocalDateTime.class),
                    modifiedTimeCriteria.getFormatToTime()));
        }
        return specification;
    }
}
