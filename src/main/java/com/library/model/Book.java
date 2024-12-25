package com.library.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private boolean available;

    // Constructeur par défaut
    public Book() {
    }
    public Book(int id, String title, String author, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
    }
    

    // Constructeur complet
    public Book(String title, String author, String publisher, int year) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    // Constructeur additionnel si nécessaire
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(int bookId, String bookTitle) {
        this.id = bookId;
        this.title = bookTitle;
    }

    public Book(String title, String author, boolean b) {
        this.title = title;
        this.author = author;
        this.available = b;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAvailable() {
        return available;
    }

    // Permet de définir la disponibilité
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isEmpty() {
        return (title == null || title.trim().isEmpty()) &&
                (author == null || author.trim().isEmpty()) &&
                id <= 0;
    }
}
