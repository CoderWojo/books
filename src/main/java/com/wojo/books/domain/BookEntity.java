package com.wojo.books.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // entity - retrieve and save BookEntity in the db 
@Table(name = "books") 
public class BookEntity {

    @Id
    private String isbn;

    private String author;

    private String title;
}
