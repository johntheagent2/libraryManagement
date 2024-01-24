package org.example.librarymanagement.service.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phoneNumber;

    private StringFilter address;

    private Boolean distinct;

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.distinct = other.distinct;
    }


    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserCriteria that = (UserCriteria) o;
        return (Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email)
        && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(address, that.address));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, address);
    }
}
