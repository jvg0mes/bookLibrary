package com.project.library.Controllers;

import com.project.library.Dtos.RentBookDto;
import com.project.library.Enums.eBookStatus;
import com.project.library.Models.Book;
import com.project.library.Models.Customer;
import com.project.library.Repositorys.BookRepository;
import com.project.library.Repositorys.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentController {

    @GetMapping("/all")
    public List<Book> getAll() {
        return BookRepository.data;
    }
    @GetMapping("/all/rented")
    public List<Book> getRentedBooks() {
        return BookRepository.data.stream().filter(x -> x.getStatus() == eBookStatus.ALUGADO).toList();
    }
    @GetMapping("/all/available")
    public List<Book> getAvailableBooks() {
        return BookRepository.data.stream().filter(x -> x.getStatus() == eBookStatus.DISPONIVEL).toList();
    }

    @GetMapping("/all/rented/customer/{cpf}")
    public List<Book> getCustomerRentedBooks(@PathVariable String cpf) {
        return CustomerRepository.findById(cpf).get(0)
                .getRentedBooks()
                .stream().map(x -> BookRepository.findById(x).get(0))
                .toList();
    }

   @PostMapping
   public ResponseEntity<String> rentBook(@RequestBody RentBookDto rentBookDto){

        List<Book> bookList = BookRepository.findById(rentBookDto.getBookId());

        if(bookList.size() == 0){
            return new ResponseEntity<>("Livro nao encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Book book = bookList.get(0);

       if(book.getStatus() == eBookStatus.ALUGADO){
           return new ResponseEntity<>("O livro ja esta alugado", HttpStatus.INTERNAL_SERVER_ERROR);
       }

       //Verificar saldo cliente
       Customer customer = CustomerRepository.findById(rentBookDto.getCustomerId()).get(0);

        if (customer.getWallet().compareTo(book.getRentPrice()) < 0){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            customer.setWallet(customer.getWallet().subtract(book.getRentPrice()));
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao debitar a conta do cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            book.setStatus(eBookStatus.ALUGADO);
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao alterar status do livro", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            book.setCurrentCustomer(customer.getCpf());
            customer.getRentedBooks().add(book.getId());
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao atribuir livro ao cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Livro alugado com sucesso", HttpStatus.OK);
   }

    @PostMapping("/giveBack/{bookId}")
    public ResponseEntity<String> giveBack(@PathVariable int bookId) {

        List<Book> bookList = BookRepository.findById(bookId);

        if (bookList.size() == 0) {
            return new ResponseEntity<>("Livro nao encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Book book = bookList.get(0);

        if (book.getStatus() == eBookStatus.DISPONIVEL) {
            return new ResponseEntity<>("O livro nao esta alugado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Customer customer = CustomerRepository.findById(book.getCurrentCustomer()).get(0);

        try {
            book.setCurrentCustomer(null);
            customer.getRentedBooks().removeIf(x -> x == bookId);
        } catch (Exception e) {
            return new ResponseEntity<>("Falha ao retirar o livro do cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            book.setStatus(eBookStatus.DISPONIVEL);
        } catch (Exception e) {
            return new ResponseEntity<>("Falha ao alterar status do livro", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Livro devolvido com sucesso", HttpStatus.OK);
    }

}
