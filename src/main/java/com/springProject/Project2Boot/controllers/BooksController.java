package com.springProject.Project2Boot.controllers;

import com.springProject.Project2Boot.models.Book;
import com.springProject.Project2Boot.models.Person;
import com.springProject.Project2Boot.services.BooksService;
import com.springProject.Project2Boot.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;

    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false,defaultValue = "0") Integer page,
                        @RequestParam(value ="books_per_page", required = false, defaultValue = "0") Integer perPage,
                        @RequestParam(value = "sort_by_year", required = false) String sort) {

        if((sort != null) || perPage > 0){
            model.addAttribute("books", booksService.paginate(page,perPage,sort));
            return "books/index";
        }

         model.addAttribute("books",booksService.findAll());
         return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult ){

       if(bindingResult.hasErrors()){
           return "books/new";
       }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable("id") int id_books){

        model.addAttribute("book", booksService.findOne(id_books));
        return "books/edit";

    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id_books ){

        if(bindingResult.hasErrors()) return "books/edit";

        booksService.updateBook(id_books, book);

        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.findOne(id));

        Person bookOwner = booksService.getBookOwner(id);

        if(booksService.getBookOwner(id) != null){

            model.addAttribute("owner",bookOwner);

        } else model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id_book){
        booksService.release(id_book);
        return "redirect:/books/" + id_book;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id_book, @ModelAttribute("person") Person person){
        booksService.assign(id_book, person.getId_person());
        return "redirect:/books/" + id_book;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String doSearch(Model model, @RequestParam(value = "query",required = false) String name){
        model.addAttribute("books", booksService.search(name));
        return "books/search";
    }
}
