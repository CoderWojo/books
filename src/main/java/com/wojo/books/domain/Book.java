package com.wojo.books.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    /*Możemy modyfikować klasę 'Book' bez konieczności zmian w BookEntity */

    private String isbn;

    private String author;

    private String title;
}
