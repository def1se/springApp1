package ru.ashitok.spring.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ashitok.spring.models.Person;
import ru.ashitok.spring.services.BooksService;
import ru.ashitok.spring.services.PeopleService;
//import ru.ashitok.spring.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final BooksService booksService;
//    private final PersonValidator personValidator;

    @Autowired  
    public PeopleController(PeopleService peopleService, BooksService booksService/*, PersonValidator personValidator*/) {
        this.peopleService = peopleService;
        this.booksService = booksService;
//        this.personValidator = personValidator;
    }

    @GetMapping()
    public String show(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String take(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", booksService.findByOwnerId(id));
        return "people/take";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("/new")
    public String newPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
