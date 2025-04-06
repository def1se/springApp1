package ru.ashitok.spring.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.ashitok.spring.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> show() {
        return jdbcTemplate.query("SELECT * FROM Person ORDER BY person_id ASC", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person take(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public Optional<Person> take(String fio) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE fio=?", new Object[]{fio}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public Person showPersonByBook(int id) {
        return jdbcTemplate.query("SELECT * FROM Person JOIN Book ON Book.person_id = Person.person_id WHERE Book.book_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public void save(Person newPerson) {
        jdbcTemplate.update("INSERT INTO Person(FIO, year_of_birth) VALUES(?, ?)", newPerson.getFio(), newPerson.getYearOfBirth());
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE Person SET fio=?, year_of_birth=? WHERE person_id=?", person.getFio(), person.getYearOfBirth(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE person_id=?", id);
    }
}
