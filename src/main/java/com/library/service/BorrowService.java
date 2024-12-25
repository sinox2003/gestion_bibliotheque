package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;

import java.util.List;

public class BorrowService {

    private final BorrowDAO borrowDAO;
    private final BookDAO bookDAO;
    private final StudentDAO studentDAO;

    public BorrowService(BorrowDAO borrowDAO, BookDAO bookDAO, StudentDAO studentDAO) {
        this.borrowDAO = borrowDAO;
        this.bookDAO = bookDAO;
        this.studentDAO = studentDAO;
    }

    public void borrowBook(Borrow borrow) {
        Student student = studentDAO.getStudentById(borrow.getStudent().getId());
        if (student == null) {
            throw new IllegalArgumentException("Étudiant ou livre non trouvé.");
        }

        Book book = bookDAO.getBookById(borrow.getBook().getId());
        if (book == null || !book.isAvailable()) {
            throw new IllegalStateException("Le livre n'est pas disponible.");
        }

        List<Borrow> existingBorrows = borrowDAO.findByBook(book);
        if (!existingBorrows.isEmpty() && existingBorrows.stream().anyMatch(b -> b.getReturnDate() == null)) {
            throw new IllegalStateException("Le livre n'est pas disponible.");
        }

        borrowDAO.addBorrow(borrow);
    }

    public void displayBorrows() {
        System.out.println("Liste des emprunts...");
    }
}