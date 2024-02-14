package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowBookRequest {

    @NotNull(message = "Id of book should not be null")
    private Long id;
}
