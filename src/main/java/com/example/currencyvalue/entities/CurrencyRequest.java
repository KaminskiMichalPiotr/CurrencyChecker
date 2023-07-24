package com.example.currencyvalue.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class CurrencyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Currency code cannot be blank")
    private String currency;
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;
    @DecimalMin(value = "0.0", message = "Value cannot be less than 0.0")
    private BigDecimal value;


    public CurrencyRequest(String name, String currency, LocalDateTime date, BigDecimal value) {
        this.name = name;
        this.currency = currency;
        this.date = date;
        this.value = value;
    }
}
