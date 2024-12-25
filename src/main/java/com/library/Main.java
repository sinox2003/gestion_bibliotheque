package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.StudentService;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection connection = DbConnection.getConnection();

        Scanner scanner = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO(connection);
        BookService bookService = new BookService(bookDAO);

        StudentDAO studentDAO = new StudentDAO(connection);
        StudentService studentService = new StudentService(studentDAO);

        BorrowDAO borrowDAO = new BorrowDAO(connection); // Connexion ajoutée au DAO
        BorrowService borrowService = new BorrowService(borrowDAO, bookDAO, studentDAO);

        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Afficher les livres");
            System.out.println("3. Ajouter un étudiant");
            System.out.println("4. Afficher les étudiants");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Afficher les emprunts");
            System.out.println("7. Quitter");

            System.out.print("Choisir une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante après l'entier

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Entrez le titre du livre: ");
                        String title = scanner.nextLine();
                        System.out.print("Entrez l'auteur du livre: ");
                        String author = scanner.nextLine();
                        Book newBook = new Book(title, author, true); // Livre disponible par défaut
                        bookService.addBook(newBook);
                        System.out.println("Livre ajouté avec succès !");
                        break;

                    case 2:
                        bookService.displayBooks();
                        break;

                    case 3:
                        System.out.print("Entrez le nom de l'étudiant: ");
                        String studentName = scanner.nextLine();
                        Student newStudent = new Student(studentName);
                        studentService.addStudent(newStudent);
                        System.out.println("Étudiant ajouté avec succès !");
                        break;

                    case 4:
                        studentService.displayStudents();
                        break;

                    case 5:
                        System.out.print("Entrez l'ID de l'étudiant: ");
                        int studentId = scanner.nextInt();
                        System.out.print("Entrez l'ID du livre: ");
                        int bookId = scanner.nextInt();

                        Student studentForBorrow = studentService.findStudentById(studentId);
                        Book bookForBorrow = bookService.findBookById(bookId);

                        if (studentForBorrow != null && bookForBorrow != null) {
                            Borrow borrow = new Borrow(studentForBorrow, bookForBorrow, new Date(), null);
                            borrowService.borrowBook(borrow);
                            System.out.println("Livre emprunté avec succès !");
                        } else {
                            System.out.println("Étudiant ou livre introuvable.");
                        }
                        break;

                    case 6:
                        borrowService.displayBorrows();
                        break;

                    case 7:
                        running = false;
                        System.out.println("Au revoir !");
                        break;

                    default:
                        System.out.println("Option invalide. Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        scanner.close();
    }
}
