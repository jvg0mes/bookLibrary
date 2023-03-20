package com.project.library.Models;

import com.project.library.Enums.eBookGender;
import com.project.library.Enums.eBookStatus;
import com.project.library.Repositorys.BookRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Collections;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id @NotNull
    int id;
    @NotNull @NotEmpty
    String name;
    String author;
    @NotNull
    eBookGender gender;
    int publishYear;
    @NotNull @Min(1)
    BigDecimal rentPrice;
    @NotNull @Min(1)
    BigDecimal sellPrice;
    @NotNull
    eBookStatus status = eBookStatus.DISPONIVEL;
    String currentCustomer;

    public Book(){
        if(BookRepository.data.size()>0) {
            this.id = Collections.max(BookRepository.data.stream().map(x -> x.getId()).toList()) + 1;
        }else{
            this.id = 1;
        }
    }

}
