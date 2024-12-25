package com.library.model;

import java.util.Date;

public class Borrow {
    private int id;
    private Student student;
    private Book book;
    private Date borrowDate;
    private Date returnDate;

    // Constructeur complet
    public Borrow(int id, Student student, Book book, Date borrowDate, Date returnDate) {
        this.id = id;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Borrow(String name, String title, Date date, Object borrowDate) {
        this.student.setName(name);
        this.book.setTitle(title);
        this.borrowDate = date;
        this.returnDate = null;
    }

    public Borrow(Student studentForBorrow, Book bookForBorrow, Date date, Object borrowDate) {
        this.student = studentForBorrow;
        this.book = bookForBorrow;
        this.borrowDate = date;
        this.returnDate = null;
    }


    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
