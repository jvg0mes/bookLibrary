package com.project.library.Controllers;

import com.project.library.Enums.eGender;
import com.project.library.Models.Customer;
import com.project.library.Repositorys.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping
    public String homeCustomer() {
        return "Welcome to customers endpoint!";
    }

    @GetMapping("/all")
    public List<Customer> getCustomers() {
        return CustomerRepository.data;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {

        try{
            return new ResponseEntity<>(CustomerRepository.findById(id).get(0),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new Customer(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerByName(@PathVariable String name){
        Customer customer = CustomerRepository.findByName(name).get(0);

        try{
            return new ResponseEntity<>(customer,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new Customer(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@Valid @RequestBody Customer customer){
        try {
                if(CustomerRepository.findById(customer.getCpf()).size()>0){
                return new ResponseEntity<>("Cliente ja existe", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                CustomerRepository.data.add(customer);
                return new ResponseEntity<>("Cliente %s criado com sucesso".formatted(customer.getCpf()), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao criar o Cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
