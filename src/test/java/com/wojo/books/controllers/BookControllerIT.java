// IT - integration test (presentation and persistance layer)
package com.wojo.books.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojo.books.TestData;
import com.wojo.books.domain.Book;
import com.wojo.books.services.BookService;

/* 1. Spring Framework - IoC 
 * 2. Spring Boot - Auto Configuration, dependency injection
*/
@SpringBootTest
@AutoConfigureMockMvc // add MockMvc object to the IoC container
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;
    
    private JacksonTester<Book> json;

    @Autowired
    private BookService bookService;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testThatBookIsCreated() throws Exception {
        final Book book = TestData.testBook();
        String isbn = book.getIsbn();
        String url = "/books/" + isbn;
        String bookJson = json.write(book).getJson();

        mockMvc.perform(MockMvcRequestBuilders.put(url).content(bookJson).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatReadReturns404WhenBookNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/123123123123"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


   @Test
   public void testThatReadReturnsHttp200AndBookWhenExists() throws Exception {
        Book book = TestData.testBook();
        bookService.save(book);   // ;)

        String url = "/books/" + book.getIsbn();

        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andExpect((MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
   }

   @Test
   public void testThatReadListReturnsHttp200EmptyListWhenNoBookExists() throws Exception{
        String url = "/books";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json("[]"));
   }

   @Test
   public void testThatReadListReturns200AndBooksWhenBooksExists() throws Exception{
        String url = "/books";
        Book book = TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(book.getTitle()))
            .andExpect(MockMvcResultMatchers.jsonPath("[0].author").value(book.getAuthor()))
            .andExpect(MockMvcResultMatchers.jsonPath("[0].isbn").value(book.getIsbn()));
   }

   @Test
   public void testThatBookIsUpdatedReturnsHttp200() throws Exception {
        Book book = TestData.testBook();
        String isbn = book.getIsbn();
        String url = "/books/" + isbn;
        book.setAuthor("Wojo"); // new author

// musimy spowodować zwrócenie 'true' przez metodę "boolean isIsbnExists(String isbn) "
        bookService.save(book);
        String bookJson = json.write(book).getJson();

       mockMvc.perform(MockMvcRequestBuilders.put(url).content(bookJson).contentType(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(MockMvcResultMatchers.status().isOk())//200
           .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn))
           .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
           .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
   }

   @Test
   public void testThatHttp204ReturnedWhenDeletePresentBook() throws Exception {
        Book book = TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + book.getIsbn()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
   }

   @Test
   public void testThatThrowedExceptionWhenDeleteAbsentBook() throws Exception {
        final String url = "/books/";
        String wrongIsbn = "9999999";
        mockMvc.perform(MockMvcRequestBuilders.delete(url + wrongIsbn))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
        
   }
}
