package com.example.currencyvalue.services;

import com.example.currencyvalue.dtos.CurrencyClientRequest;
import com.example.currencyvalue.dtos.CurrencyRate;
import com.example.currencyvalue.entities.CurrencyRequest;
import com.example.currencyvalue.exceptions.CurrencyNotFoundException;
import com.example.currencyvalue.exceptions.CurrencyRatesProcessingException;
import com.example.currencyvalue.repositories.CurrencyRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CurrencyValueService {

    private static final String NBP_API_URL = "https://api.nbp.pl/api/exchangerates/tables/A?format=json";

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final CurrencyRequestRepository currencyRequestRepository;

    private final Clock clock;

    private void saveCurrencyValueRequest(@Valid CurrencyRequest currencyRequest){
        currencyRequestRepository.save(currencyRequest);
    }

    public BigDecimal getCurrencyRate(CurrencyClientRequest currencyClientRequest) {
        //Find if currency exists.
        List<CurrencyRate> currencyRates = getCurrencyRates();
        Optional<CurrencyRate> currencyRate = currencyRates
                .stream()
                .filter(c -> c.getCode().equals(currencyClientRequest.getCurrency()))
                .findFirst();
        BigDecimal rate = currencyRate
                .orElseThrow(() -> new CurrencyNotFoundException(
                        "ERROR: Currency=" + currencyClientRequest.getCurrency() + " not found"))
                .getMid();
        //Save correct request.
        CurrencyRequest currencyRequest = new CurrencyRequest(
                currencyClientRequest.getName(),
                currencyClientRequest.getCurrency(),
                LocalDateTime.now(clock),
                rate);
        saveCurrencyValueRequest(currencyRequest);
        //Return response.
        return rate;
    }

    private List<CurrencyRate> getCurrencyRates() {
        try {
            // Make an HTTP request to the NBP API and fetch data in JSON format.
            ResponseEntity<String> response = restTemplate.getForEntity(NBP_API_URL, String.class);

            // Parsing the JSON response into a list of CurrencyRates objects.
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            List<CurrencyRate> currencyRateList = new ArrayList<>();
            JsonNode ratesNode = rootNode.get(0).get("rates");
            for (JsonNode rateNode : ratesNode) {
                String currency = rateNode.get("currency").asText();
                String code = rateNode.get("code").asText();
                BigDecimal mid = rateNode.get("mid").decimalValue();

                CurrencyRate currencyRate = new CurrencyRate(currency, code, mid);
                currencyRateList.add(currencyRate);
            }

            return currencyRateList;
        } catch (RestClientException e){
            throw new CurrencyRatesProcessingException("ERROR: couldn't access currency rates at: " + NBP_API_URL);
        } catch (JsonProcessingException e) {
            throw new CurrencyRatesProcessingException("ERROR: couldn't process NBP rates response");
        }
    }

    public List<String> getCurrencyCodes(){
        List<CurrencyRate> currencyRates = getCurrencyRates();
        return currencyRates
                .stream()
                .map(CurrencyRate::getCode)
                .collect(Collectors.toList());
    }

    public List<CurrencyRequest> getCurrencyRequests() {
        return currencyRequestRepository.findAll();
    }


}
