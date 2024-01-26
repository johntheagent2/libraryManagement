package org.example.librarymanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public class CreateAuthorRequest {

    @NotNull(message = "Name should not be empty!")
    @Length(max = 50, message = "Name should be max 100 characters")
    private String name;

    @NotNull(message = "Nationality should not be empty!")
    @Length(max = 50, message = "Nationality should be max 100 characters")
    private String nationality;


    private LocalDate birthDay;

    private String biography;
}
