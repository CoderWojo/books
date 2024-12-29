package com.wojo.books.services;

import java.util.List;
import java.util.Optional;

import com.wojo.books.domain.Book;

public interface BookService {

    // service layer is responsible for verifying wheter the book exists or not
    boolean isIsbnExists(String isbn); 

    Book save(Book book);   // create or update

    Optional<Book> readBookById(String isbn);

    List<Book> readList();

    void deleteBookById(String isbn);
}