package com.springProject.Project2Boot.controllers;

import com.springProject.Project2Boot.models.Person;
import com.springProject.Project2Boot.services.PeopleService;
import com.springProject.Project2Boot.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final PersonValidator personValidator;

    @Autowired
    public PeopleController( PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id){

        Person person = peopleService.show(id).orElse(null);

        model.addAttribute("person", person);
        model.addAttribute("books", peopleService.showBooks(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person")Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){

        //personValidator.validate(person,bindingResult);

        if(bindingResult.hasErrors()){
            return "people/new";
        }

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){

        Person person = peopleService.show(id).orElse(null);

        model.addAttribute("person",person);
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult, @PathVariable("id") int id_person){

        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors()){
            return "people/edit";
        }

        peopleService.update(id_person, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id_person){
        peopleService.delete(id_person);
        return "redirect:/people";
    }
}
