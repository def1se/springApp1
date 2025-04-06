package ru.ashitok.spring.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.ashitok.spring.models.Book;
import ru.ashitok.spring.models.Person;

import java.util.List;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> show() {
        return jdbcTemplate.query("SELECT * FROM Book ORDER BY book_id ASC", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book take(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE book_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    public List<Book> showByPerson(int id) {
        return jdbcTemplate.query("SELECT * FROM Book JOIN Person ON Person.person_id = Book.person_id WHERE Person.person_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

    public void appointPerson(Person person, int bookId) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE book_id=?", person.getPersonId(), bookId);
    }

    public void save(Book newBook) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year_of_manufacture) VALUES(?, ?, ?)", newBook.getTitle(), newBook.getAuthor(), newBook.getYearOfManufacture());
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE book_id=?", id);
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year_of_manufacture=? WHERE book_id=?", book.getTitle(), book.getAuthor(), book.getYearOfManufacture(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?", id);
    }
}
