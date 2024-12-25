package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private BookService bookService;

    @Mock
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookDAO);
    }

    @Test
    public void testAddBook() {
        Book book = new Book(1, "Java Programming", "John Doe", true);

        List<Book> bookList = new ArrayList<>();
        when(bookDAO.getAllBooks()).thenReturn(bookList);
        when(bookDAO.getBookById(1)).thenReturn(book);

        bookService.addBook(book);

        verify(bookDAO).add(book);
        assertEquals(0, bookList.size());
        assertEquals("Java Programming", bookDAO.getBookById(1).getTitle());
    }

    @Test
    public void testUpdateBook() {
        Book originalBook = new Book(2, "Java Programming", "John Doe", true);
        Book updatedBook = new Book(2, "Advanced Java", "Jane Doe", false);

        when(bookDAO.getBookById(2)).thenReturn(updatedBook);
        bookService.updateBook(updatedBook);
        verify(bookDAO).update(updatedBook);
        assertEquals("Advanced Java", bookDAO.getBookById(2).getTitle());
        assertFalse(bookDAO.getBookById(2).isAvailable());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book(4, "Java Programming", "John Doe", true);
        bookService.deleteBook(4);
        verify(bookDAO).delete(4);
    }
}