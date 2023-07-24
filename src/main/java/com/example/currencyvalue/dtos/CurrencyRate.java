package com.example.currencyvalue.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrencyRate {

    private String currency;
    private String code;
    private BigDecimal mid;

}
