
package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    private final Connection connection;
    private final StudentDAO studentDAO;
    private final BookDAO bookDAO;

    public BorrowDAO(Connection connection) throws SQLException {
        this.connection = DbConnection.getConnection();
        this.studentDAO = new StudentDAO(this.connection);
        this.bookDAO = new BookDAO(this.connection);
    }

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    int bookId = rs.getInt("book_id");
                    Student student = studentDAO.getStudentById(studentId);

                    Book book = bookDAO.getBookById(bookId);
                    Borrow borrow = new Borrow(
                            rs.getInt("id"),
                            student,
                            book,
                            rs.getDate("borrow_date"),
                            rs.getDate("return_date")
                    );
                    borrows.add(borrow);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    public void addBorrow(Borrow borrow) {
        String query = "INSERT INTO borrows (student_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));

            // Gérer la date de retour
            if (borrow.getReturnDate() != null) {
                stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE); // Si null, utilisez setNull
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Borrow> findByBook(Book book) {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, book.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    Student student = studentDAO.getStudentById(studentId);
                    Borrow borrow = new Borrow(
                            rs.getInt("id"),
                            student,
                            book,
                            rs.getDate("borrow_date"),
                            rs.getDate("return_date")
                    );
                    borrows.add(borrow);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    public void save(Borrow borrow) {
        List<Borrow> existingBorrows = findByBook(borrow.getBook());

        // Vérifier si le livre est déjà emprunté
        boolean isBookCurrentlyBorrowed = existingBorrows.stream()
                .anyMatch(existingBorrow -> existingBorrow.getReturnDate() == null);
        // Si le livre est déjà emprunté, lancer une exception
        if (isBookCurrentlyBorrowed) {
            throw new IllegalStateException("Le livre n'est pas disponible.");
        }

        addBorrow(borrow);
    }

    private boolean borrowExists(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM borrows WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void updateBorrow(Borrow borrow) throws SQLException {
        String query = "UPDATE borrows SET student_id = ?, book_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());

            stmt.setDate(3, borrow.getBorrowDate() != null
                    ? new java.sql.Date(borrow.getBorrowDate().getTime())
                    : null);

            stmt.setDate(4, borrow.getReturnDate() != null
                    ? new java.sql.Date(borrow.getReturnDate().getTime())
                    : null);

            stmt.setInt(5, borrow.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Mise à jour de l'emprunt échouée");
            }
        }
    }
}
