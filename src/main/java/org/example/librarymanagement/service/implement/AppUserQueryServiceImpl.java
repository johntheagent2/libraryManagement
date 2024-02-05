package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.AppUser_;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.criteria.UserCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;

@AllArgsConstructor
@Service
public class AppUserQueryServiceImpl extends QueryService<AppUser> {

    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public Page<UserResponse> findByCriteria(UserCriteria criteria, Pageable page) {
        final Specification<AppUser> specification = createSpecification(criteria);
        Page<AppUser> appUsers = appUserRepository.findAll(specification, page);
        // Map AppUser to UserResponse
        return appUsers.map((appUser) -> UserResponse.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .address(appUser.getAddress())
                .email(appUser.getEmail())
                .phoneNumber(appUser.getPhoneNumber())
                .build());

    }

    private Specification<AppUser> createSpecification(UserCriteria criteria) {
        Specification<AppUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), AppUser_.firstName));
            }
            if(criteria.getLastName() != null) {
                specification = specification.and(buildSpecification(criteria.getLastName(), AppUser_.lastName));
            }
            if(criteria.getAddress() != null) {
                specification = specification.and(buildSpecification(criteria.getAddress(), AppUser_.address));
            }
            if(criteria.getEmail() != null) {
                specification = specification.and(buildSpecification(criteria.getEmail(), AppUser_.email));
            }
            if(criteria.getPhoneNumber() != null) {
                specification = specification.and(buildSpecification(criteria.getPhoneNumber(), AppUser_.phoneNumber));
            }
        }
        return specification;
    }
}
