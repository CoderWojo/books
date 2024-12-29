package com.wojo.books.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wojo.books.domain.Book;
import com.wojo.books.domain.BookEntity;
import com.wojo.books.exceptions.BookNotFoundException;
import com.wojo.books.repositories.BookRepository;
import com.wojo.books.services.BookService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> readBookById(String isbn) {
        Optional<BookEntity> foundBookEntity = bookRepository.findById(isbn);
        Optional<Book> foundBook = foundBookEntity.map((BookEntity b) -> bookEntityToBook(b));    // try convert value in Optional using mapper function
        
        return foundBook;
    }
    
    @Override
    public List<Book> readList() {
        List<BookEntity> foundBookEntities = bookRepository.findAll();
        return foundBookEntities.stream()
            .map((BookEntity b) -> bookEntityToBook(b))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isIsbnExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public void deleteBookById(String isbn) {
        //if isbn does not exist, thorws Exception
        if(!bookRepository.existsById(isbn)) {
            BookNotFoundException b = new BookNotFoundException("Book with ISBN " + isbn + " not found!", isbn);
            log.debug("Attempting to delete non-existing book!", b);
            throw b;
        } else
            bookRepository.deleteById(isbn);
    }

    private BookEntity bookToBookEntity(Book book) {
        return BookEntity.builder()
            .author(book.getAuthor())
            .isbn(book.getIsbn())
            .title(book.getTitle())
            .build();
    }

    @Override
    public Book save(Book book) {
        BookEntity bookEntity = bookToBookEntity(book);
        bookRepository.save(bookEntity);

        return bookEntityToBook(bookEntity);
    }

    private Book bookEntityToBook(BookEntity bookEntity) {
        return Book.builder()
            .isbn(bookEntity.getIsbn())
            .author(bookEntity.getAuthor())
            .title(bookEntity.getTitle())
            .build();
    }
}
