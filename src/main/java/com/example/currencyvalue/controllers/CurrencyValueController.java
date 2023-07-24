package com.example.currencyvalue.controllers;


import com.example.currencyvalue.dtos.CurrencyClientRequest;
import com.example.currencyvalue.entities.CurrencyRequest;
import com.example.currencyvalue.services.CurrencyValueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("currencies")
public class CurrencyValueController {

    private CurrencyValueService currencyValueService;

    @PostMapping("/get-current-currency-value-command")
    public ResponseEntity<BigDecimal> getCurrencyRate(@Valid @RequestBody CurrencyClientRequest currencyClientRequest){
        BigDecimal currencyRate = currencyValueService.getCurrencyRate(currencyClientRequest);
        return new ResponseEntity<>(currencyRate, HttpStatus.OK);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<CurrencyRequest>> getCurrencyRequests(){
        return new ResponseEntity<>(currencyValueService.getCurrencyRequests(), HttpStatus.OK);
    }

    @GetMapping("/codes")
    public ResponseEntity<List<String>> getCurrencyCodes(){
        return new ResponseEntity<>(currencyValueService.getCurrencyCodes(), HttpStatus.OK);
    }

}
