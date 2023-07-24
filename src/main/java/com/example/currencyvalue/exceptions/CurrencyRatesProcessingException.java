package com.example.currencyvalue.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CurrencyRatesProcessingException extends RuntimeException{

    public CurrencyRatesProcessingException(String message) {
        super(message);
    }
}
