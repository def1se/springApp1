package ru.ashitok.spring.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ashitok.spring.models.Book;
import ru.ashitok.spring.models.Person;
import ru.ashitok.spring.repositories.BooksRepository;
import ru.ashitok.spring.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("yearOfManufacture"));
        } else {
            return booksRepository.findAll();
        }
    }

    /////////////////////////////////////////////////
    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfManufacture"))).getContent(); //getContent() - чтобы получить все в виде списка
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent(); //это если пользователь не хочет сортиров
    }
    /// /////////////////////////////////////////////

    public List<Book> searchByTitle(String title) {
        return booksRepository.findBookByTitleStartingWith(title);
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book newBook) {
        booksRepository.save(newBook);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        Book bookToBeUp = booksRepository.findById(id).get();

        updateBook.setId(id);
        updateBook.setOwner(bookToBeUp.getOwner());

        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findByOwnerId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(booksRepository.findByOwner(person.get()));

            booksRepository.findByOwner(person.get()).forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if (diffInMillies > 60000)
                    book.setExpired(true);
            });

            return booksRepository.findByOwner(person.get());
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void appointPerson(Person person, int bookId) {
        Book book = findOne(bookId);
        book.setOwner(person);
        book.setTakenAt(new Date());
    }

    @Transactional
    public void releaseBook(int bookId) {
        Book book = findOne(bookId);
        book.setOwner(null);
        book.setTakenAt(null);
    }
}
