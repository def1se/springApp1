package ru.ashitok.spring.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class Book {
    private int bookId;

    @NotEmpty(message = "Название не должно быть пустым")
    private String title;

    @NotEmpty(message = "Автор не должен быть пустым")
    private String author;

    @Min(value = 1500, message = "Год должен быть больше, чем 1500")
    private int yearOfManufacture;

    public Book(int bookId, String title, String author, int yearOfManufacture) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.yearOfManufacture = yearOfManufacture;
    }

    public Book(){}

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }
}
