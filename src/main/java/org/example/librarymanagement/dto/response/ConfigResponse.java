package org.example.librarymanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigResponse {

    private String key;
    private String value;
}
