package com.wojo.books.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wojo.books.TestData;
import com.wojo.books.domain.Book;
import com.wojo.books.domain.BookEntity;
import com.wojo.books.repositories.BookRepository;

// Pozwala na używanie mocków w testacj JUnit5, inaczej - integruje Mockowanie z JUNIT
// Testy jednostkowe
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    
    @Mock   // beacuse we want to test only BookServiceImpl class
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;
    
    @Test
    public void testThatBookIsSaved() {
        final Book book = Book.builder()
            .author("Wirginia Woolf")
            .isbn("02345678")
            .title("The Waves")
            .build();

        // w metodzie create() konwertujemy na BookEntity i wywołujemy create
        final BookEntity bookEntity = BookEntity.builder()
            .author("Wirginia Woolf")
            .isbn("02345678")
            .title("The Waves")
            .build();
        
        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test   
    public void TestThatReadReturnsEmptyWhenNoBook() {
        final String isbn = "123123123123"; // no exists
        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.empty());
        
        Optional<Book> result = underTest.readBookById(isbn);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void TestThatReadReturnsBookWhenExists() {
        BookEntity bookEntity = TestData.testBookEntity();
        Optional<Book> book = Optional.of(TestData.testBook()); // expected
        String isbn = book.get().getIsbn();

        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.of(bookEntity));
        
        Optional<Book> result = underTest.readBookById(isbn);
        assertEquals(book, result);
    }

    @Test
    public void testThatReadListReturnsEmptyListWhenNoBookExists() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<BookEntity>());
        List<Book> result = underTest.readList();
        assertEquals(0, result.size());
    }

    @Test
    public void testThatReadListReturnsBooksWhenExists() {
        BookEntity bookEntity = TestData.testBookEntity();
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));

        List<Book> result = underTest.readList();
        assertEquals(1, result.size());
    }

    @Test
    public void testThatIsBookExistsReturnsTrueWhenExists() {
        Book book = TestData.testBook();

        when(bookRepository.existsById(eq(book.getIsbn()))).thenReturn(Boolean.valueOf(true));
        boolean result = underTest.isIsbnExists(book.getIsbn());

        assertEquals(true, result);
    }

    @Test
    public void testThatIsBookExistsReturnsFalseWhenNoExists() {
        Book book = TestData.testBook();
        when(bookRepository.existsById(any())).thenReturn(false);

        boolean result = underTest.isIsbnExists(book.getIsbn());
        assertEquals(false, result);
    }

    @Test
    public void testThatPresentBookIsDeleted() {
        Book book = TestData.testBook();
        BookEntity bookEntity = TestData.testBookEntity();
        String isbn = book.getIsbn();

        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);
        when(bookRepository.existsById(isbn)).thenReturn(true);

        underTest.save(book);
        underTest.deleteBookById(isbn);

        verify(bookRepository, VerificationModeFactory.times(1)).deleteById(eq(isbn));   
    }
}
