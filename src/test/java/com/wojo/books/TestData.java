package com.wojo.books;

import com.wojo.books.domain.Book;
import com.wojo.books.domain.BookEntity;

public class TestData {
    
    private TestData() {
        // clas no for creating 
    }

    public static Book testBook() {
        return Book.builder()
        .author("Wirginia Woolf")
        .isbn("02345678")
        .title("The Waves")
        .build();
    }

    public static BookEntity testBookEntity() {
        return BookEntity.builder()
        .author("Wirginia Woolf")
        .isbn("02345678")
        .title("The Waves")
        .build();
    }
}
