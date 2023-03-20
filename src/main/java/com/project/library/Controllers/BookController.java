package com.project.library.Controllers;

import com.project.library.Models.Book;
import com.project.library.Models.Customer;
import com.project.library.Repositorys.BookRepository;
import com.project.library.Repositorys.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @GetMapping
    public String homeCustomer() {
        return "Welcome to books endpoint!";
    }

    @GetMapping("/all")
    public List<Book> getCustomers() {
        return BookRepository.data;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Book> getBookById(@PathVariable int id) {

        try{
            return new ResponseEntity<>(BookRepository.findById(id).get(0), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new Book(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public ResponseEntity<List<Book>> getCustomerByName(@PathVariable String name){

        try{
            return new ResponseEntity<>(BookRepository.findByName(name),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@Valid @RequestBody Book book){
        try {
            if(BookRepository.findById(book.getId()).size() > 0){
                return new ResponseEntity<>("Livro ja existe", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            BookRepository.data.add(book);
            return new ResponseEntity<>("Livro %s criado com sucesso".formatted(book.getName()), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao criar o livro", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
