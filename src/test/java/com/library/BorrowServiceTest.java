package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceTest {
    private BorrowService borrowService;
    private BorrowDAO borrowDAO;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        borrowDAO = mock(BorrowDAO.class);
        bookDAO = mock(BookDAO.class);
        studentDAO = mock(StudentDAO.class);

        borrowService = new BorrowService(borrowDAO, bookDAO, studentDAO);
    }

    @Test
    void testBorrowBook() {
        Student student = new Student(1, "Alice");
        Book book = new Book(1, "Java Programming", "John Doe", true);
        Date borrowDate = new Date();
        Borrow borrow = new Borrow(1, student, book, borrowDate, null);

        when(studentDAO.getStudentById(student.getId())).thenReturn(student);
        when(bookDAO.getBookById(book.getId())).thenReturn(book);
        when(borrowDAO.findByBook(book)).thenReturn(new ArrayList<>());

        borrowService.borrowBook(borrow);

        verify(borrowDAO).addBorrow(borrow);
    }

    @Test
    void testBorrowBookNotAvailable() {
        Student student = new Student(1, "Alice");
        Book book = new Book(1, "Java Programming", "John Doe", false);
        Date borrowDate = new Date();
        Borrow borrow = new Borrow(1, student, book, borrowDate, null);

        when(studentDAO.getStudentById(student.getId())).thenReturn(student);
        when(bookDAO.getBookById(book.getId())).thenReturn(book);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> borrowService.borrowBook(borrow)
        );

        assertEquals("Le livre n'est pas disponible.", exception.getMessage());
        verify(borrowDAO, never()).addBorrow(any());
    }

    @Test
    void testBorrowBookStudentNotFound() {
        Book book = new Book(1, "Java Programming", "John Doe", true);
        Date borrowDate = new Date();
        Student student = new Student(1, "Non Existent");
        Borrow borrow = new Borrow(1, student, book, borrowDate, null);

        when(studentDAO.getStudentById(student.getId())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> borrowService.borrowBook(borrow)
        );

        assertEquals("Étudiant ou livre non trouvé.", exception.getMessage());
        verify(borrowDAO, never()).addBorrow(any());
    }

}