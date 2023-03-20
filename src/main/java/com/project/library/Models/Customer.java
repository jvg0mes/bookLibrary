package com.project.library.Models;

import com.project.library.Enums.eGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id @NotNull @NotEmpty @NotBlank
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")
    String cpf;
    @NotNull @NotEmpty @NotBlank
    String name;
    @NotNull
    LocalDate birthDate;
    @NotNull
    BigDecimal income;
    @NotNull
    @Enumerated(EnumType.STRING)
    eGender gender;
    @NotNull
    BigDecimal wallet;
    @NotNull
    List<Integer> rentedBooks = new ArrayList<>();

}
