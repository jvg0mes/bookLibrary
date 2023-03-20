package com.project.library.Repositorys;

import com.project.library.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository{

    public static List<Customer> data = new ArrayList<>();

    public static List<Customer> findById(String cpf){
        return data.stream().filter(x -> x.getCpf().equals(cpf)).toList();
    }

    public static List<Customer> findByName(String name){
        return data.stream().filter(x -> x.getName().equals(name)).toList();
    }

}
