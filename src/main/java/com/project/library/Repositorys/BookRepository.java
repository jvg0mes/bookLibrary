package com.project.library.Repositorys;

import com.project.library.Models.Book;
import com.project.library.Models.Customer;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public static List<Book> data = new ArrayList<>();

    public static List<Book> findById(int id){
        return data.stream().filter(x -> x.getId() == id).toList();
    }

    public static List<Book> findByName(String name){
        return data.stream().filter(x -> x.getName().equals(name)).toList();
    }

}
