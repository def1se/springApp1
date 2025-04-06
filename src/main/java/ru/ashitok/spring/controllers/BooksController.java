package ru.ashitok.spring.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ashitok.spring.dao.BookDAO;
import ru.ashitok.spring.dao.PersonDAO;
import ru.ashitok.spring.models.Book;
import ru.ashitok.spring.models.Person;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String show(Model model) {
        model.addAttribute("books", bookDAO.show());
        return "books/show";
    }

    @GetMapping("/{id}")
    public String take(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.take(id));
        model.addAttribute("person", personDAO.showPersonByBook(id));
        model.addAttribute("people", personDAO.show());

        return "books/take";
    }

    @PostMapping("/choose")
    public String choosePerson(@ModelAttribute("person") Person person, @RequestParam("bookId") int bookId) {
        bookDAO.appointPerson(person, bookId);
        return "redirect:/books";
    }

    @PostMapping("/release")
    public String releaseBook(@RequestParam("bookId") int bookId) {
        bookDAO.release(bookId);
        return "redirect:/books";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping("/new")
    public String newBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.take(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }
}
