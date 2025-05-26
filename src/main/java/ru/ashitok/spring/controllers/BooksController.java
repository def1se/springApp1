package ru.ashitok.spring.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ashitok.spring.models.Book;
import ru.ashitok.spring.models.Person;
import ru.ashitok.spring.services.BooksService;
import ru.ashitok.spring.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    /// ///////////////////////////////
    @GetMapping()
    public String show(Model model, @RequestParam(value = "page", required = false) Integer page, //required = false означает, что параметр необязательный, его можно не передавать
                       @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                       @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if (page == null || booksPerPage == null) {
            model.addAttribute("books", booksService.findAll(sortByYear));
        } else {
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage, sortByYear));
        }

        return "books/show";
    }

    /// ///////////////////////////////

    @GetMapping("/{id}")
    public String take(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        model.addAttribute("person", peopleService.findPersonByBookId(id).orElse(null));
        model.addAttribute("people", peopleService.findAll());

        return "books/take";
    }

    @PostMapping("/choose")
    public String choosePerson(@RequestParam("personId") int personId, @RequestParam("bookId") int bookId) {
        Person person = peopleService.findOne(personId);
        booksService.appointPerson(person, bookId);
        return "redirect:/books";
    }

    @PostMapping("/release")
    public String releaseBook(@RequestParam("bookId") int bookId) {
        booksService.releaseBook(bookId);
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

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }


    @GetMapping("/search")
    public String searchPage() {return "books/search";}

    @PostMapping("/search")
    public String findBook(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", booksService.searchByTitle(query));

        return "books/search";
    }

}
