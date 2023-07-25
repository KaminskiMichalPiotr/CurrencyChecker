package com.example.currencyvalue.services;

import com.example.currencyvalue.dtos.CurrencyClientRequest;
import com.example.currencyvalue.entities.CurrencyRequest;
import com.example.currencyvalue.exceptions.CurrencyNotFoundException;
import com.example.currencyvalue.exceptions.CurrencyRatesProcessingException;
import com.example.currencyvalue.repositories.CurrencyRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CurrencyValueServiceTest {

    //Mocked response from NBP api
    public static final String JSON_NBP_API_RESPONSE = "[{\"table\":\"A\",\"no\":\"141/A/NBP/2023\",\"effectiveDate\":\"2023-07-24\"," +
            "\"rates\":[{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0210},{\"currency\":\"euro\",\"code\":\"EUR\"," +
            "\"mid\":4.4517},{\"currency\":\"funt szterling\",\"code\":\"GBP\",\"mid\":5.1527}]}]";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyRequestRepository currencyRequestRepository;

    @InjectMocks
    private CurrencyValueService currencyValueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Clock fixedClock = Clock.fixed(Instant.parse("2023-07-24T12:00:00Z"), ZoneId.systemDefault());
        ObjectMapper objectMapper = new ObjectMapper();
        currencyValueService = new CurrencyValueService(
                restTemplate, objectMapper, currencyRequestRepository, fixedClock
        );
    }

    @Test
    void testGetCurrencyRate_ExistingCurrency_Success() {
        CurrencyClientRequest request = new CurrencyClientRequest("USD", "John Doe");

        // Mock the response from NBP API
        ResponseEntity<String> responseEntity = ResponseEntity.ok(JSON_NBP_API_RESPONSE);
        when(restTemplate.getForEntity(eq("https://api.nbp.pl/api/exchangerates/tables/A?format=json"), eq(String.class)))
                .thenReturn(responseEntity);

        when(currencyRequestRepository.save(any(CurrencyRequest.class))).thenReturn(null);

        BigDecimal rate = currencyValueService.getCurrencyRate(request);

        assertEquals(BigDecimal.valueOf(4.0210), rate);
    }

    @Test
    void testGetCurrencyRate_NonExistingCurrency_ThrowsException() {
        CurrencyClientRequest request = new CurrencyClientRequest("ABC", "Jane Smith");

        // Mock the response from NBP API
        ResponseEntity<String> responseEntity = ResponseEntity.ok(JSON_NBP_API_RESPONSE);
        when(restTemplate.getForEntity(eq("https://api.nbp.pl/api/exchangerates/tables/A?format=json"), eq(String.class)))
                .thenReturn(responseEntity);

        assertThrows(CurrencyNotFoundException.class, () -> currencyValueService.getCurrencyRate(request));
    }

    @Test
    void testGetCurrencyRate_RestClientException_ThrowsException() {
        CurrencyClientRequest request = new CurrencyClientRequest("USD", "John Doe");

        when(restTemplate.getForEntity(anyString(), any())).thenThrow(new RestClientException("Connection error"));

        assertThrows(CurrencyRatesProcessingException.class, () -> currencyValueService.getCurrencyRate(request));
    }

    @Test
    void testGetCurrencyCodes() {
        // Mock the response from NBP API
        ResponseEntity<String> responseEntity = ResponseEntity.ok(JSON_NBP_API_RESPONSE);
        when(restTemplate.getForEntity(eq("https://api.nbp.pl/api/exchangerates/tables/A?format=json"), eq(String.class)))
                .thenReturn(responseEntity);

        // Call the method to get the currency codes.
        List<String> currencyCodes = currencyValueService.getCurrencyCodes();

        // Assert the expected currency codes.
        assertEquals(3, currencyCodes.size());
        assertEquals("USD", currencyCodes.get(0));
        assertEquals("EUR", currencyCodes.get(1));
        assertEquals("GBP", currencyCodes.get(2));
    }

    @Test
    void testGetCurrencyRequests() {
        List<CurrencyRequest> expectedCurrencyRequests = new ArrayList<>();
        expectedCurrencyRequests.add(new CurrencyRequest("John Doe", "USD", null, BigDecimal.valueOf(4.0210)));
        expectedCurrencyRequests.add(new CurrencyRequest("Jane Smith", "EUR", null, BigDecimal.valueOf(4.4517)));

        // Mocking the findAll() method in the currencyRequestRepository to return the list of currency requests.
        when(currencyRequestRepository.findAll()).thenReturn(expectedCurrencyRequests);

        // Call the method to get the currency requests.
        List<CurrencyRequest> currencyRequests = currencyValueService.getCurrencyRequests();

        // Assert the expected currency requests.
        assertEquals(expectedCurrencyRequests.size(), currencyRequests.size());
        assertEquals(expectedCurrencyRequests, currencyRequests);
    }
}