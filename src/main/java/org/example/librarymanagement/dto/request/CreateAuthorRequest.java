package org.example.librarymanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.common.validator.datevalidator.PastLocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthorRequest {

    @NotNull(message = "Name should not be null")
    private String name;

    @NotNull(message = "Nationality should not be null")
    private String nationality;

    @NotNull(message = "Birthday should not be null")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastLocalDate
    private LocalDate birthDay;

    @NotNull(message = "Biography should not be null")
    private String biography;
}
