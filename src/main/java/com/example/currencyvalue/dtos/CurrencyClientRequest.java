package com.example.currencyvalue.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrencyClientRequest {

    @NotBlank(message = "Currency code must not be blank")
    @Pattern(regexp = "[A-Z]{3}", message = "Invalid currency code format. Use three uppercase letters.")
    private String currency;

    @NotBlank(message = "Name must not be blank")
    private String name;

}
