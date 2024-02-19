package org.example.librarymanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookInvoiceResponse {
    private String image;
    private String title;
    private double price;
}
