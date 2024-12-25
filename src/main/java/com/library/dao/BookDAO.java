package com.library.dao;

import com.library.model.Book;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public BookDAO(Connection connection) throws SQLException {
        connection = DbConnection.getConnection();
    }

    public void add(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, year) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Livre inséré avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    // Récupérer un livre par son ISBN
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = null;
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setYear(resultSet.getInt("year"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
        }
        
        return book;
    }
    
    // Récupérer tous les livres
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection connection = DbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
             
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        
        return books;
    }

    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Livre supprimé avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }

    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setInt(5, book.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Livre mis à jour avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }


    public boolean isBookAvailable(int id) {
        String sql = "SELECT COUNT(*) AS available FROM books b " +
                "LEFT JOIN borrows br ON b.id = br.book_id " +
                "WHERE b.id = ? AND br.return_date IS NOT NULL OR br.book_id IS NULL";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int availableCount = resultSet.getInt("available");
                    return availableCount > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la disponibilité du livre : " + e.getMessage());
        }

        return false;
    }

    public void updateBookAvailability(int id, boolean available) {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, available);
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Disponibilité du livre mise à jour avec succès !");
            } else {
                System.out.println("Aucun livre trouvé avec cet identifiant.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la disponibilité du livre : " + e.getMessage());
        }
    }
}
