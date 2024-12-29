package com.wojo.books.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wojo.books.domain.Book;
import com.wojo.books.services.BookService;

@RestController
public class BookController {
    
    private BookService bookService;

    public BookController(final BookService bookService) {  // from IoC (@Service) (com.wojo.books.services.impl.BookServiceImpl)
        this.bookService = bookService;
    }

    @PutMapping(path = "/books/{isbn}") // create or update
    public ResponseEntity<Book> createUpdateBook(@PathVariable String isbn, @RequestBody Book book) {
        book.setIsbn(isbn); // if isbn from json is diffrent from url, take the isbn from url
        boolean isBookExists = bookService.isIsbnExists(isbn);
        Book savedBook = bookService.save(book);

        if(isBookExists) {
            // update
            return new ResponseEntity<Book>(savedBook, HttpStatus.OK);    
        } else {
            //create
            return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
        } 
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> readBook(@PathVariable String isbn) {
        Optional<Book> foundBook = bookService.readBookById(isbn);

        return foundBook.map((Book b) -> new ResponseEntity<>(b, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> readListOfBooks() {
        List<Book> books = bookService.readList();

        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        bookService.deleteBookById(isbn);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
}
