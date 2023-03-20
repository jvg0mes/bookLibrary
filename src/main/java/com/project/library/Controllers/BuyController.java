package com.project.library.Controllers;

import com.project.library.Dtos.BuyBookDto;
import com.project.library.Dtos.RentBookDto;
import com.project.library.Enums.eBookStatus;
import com.project.library.Models.Book;
import com.project.library.Models.Customer;
import com.project.library.Repositorys.BookRepository;
import com.project.library.Repositorys.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/buy")
public class BuyController {

    @PostMapping
    public ResponseEntity<String> rentBook(@RequestBody BuyBookDto buyBookDto){

        List<Book> bookList = BookRepository.findById(buyBookDto.getBookId());

        if(bookList.size() == 0){
            return new ResponseEntity<>("Livro nao encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Book book = bookList.get(0);

        if(book.getStatus() == eBookStatus.ALUGADO){
            return new ResponseEntity<>("O livro esta alugado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Verificar saldo cliente
        Customer customer = CustomerRepository.findById(buyBookDto.getCustomerId()).get(0);

        if (customer.getWallet().compareTo(book.getRentPrice()) < 0){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            customer.setWallet(customer.getWallet().subtract(book.getSellPrice()));
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao debitar a conta do cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            BookRepository.data.removeIf(x -> x.getId() == book.getId());
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao atribuir livro ao cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Livro comprado com sucesso", HttpStatus.OK);
    }

}
