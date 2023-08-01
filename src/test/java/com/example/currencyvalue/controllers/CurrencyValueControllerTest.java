package com.example.currencyvalue.controllers;

import com.example.currencyvalue.dtos.CurrencyClientRequest;
import com.example.currencyvalue.entities.CurrencyRequest;
import com.example.currencyvalue.repositories.CurrencyRequestRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Tests are run on h2 database

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CurrencyValueControllerTest {

    public static final double USD_RATE = 4.0210;
    public static final double EUR_RATE = 4.4517;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyRequestRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrencyRate() throws Exception {
        CurrencyClientRequest request = new CurrencyClientRequest("USD", "Jan Kowalski");

        MvcResult result = mockMvc
                .perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(jsonNode.has("value"), "The 'value' field is missing in the JSON response");
        JsonNode valueNode = jsonNode.get("value");
        assertTrue(valueNode.isNumber(), "The 'value' field is not a number");
    }

    @Test
    void testGetCurrencyRate_IncorrectName_ThrowsException() throws Exception {
        CurrencyClientRequest request = new CurrencyClientRequest("USD", "");

        mockMvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void testGetCurrencyRate_IncorrectCurrency_ThrowsException() throws Exception {
        CurrencyClientRequest request = new CurrencyClientRequest("XXX", "Jan Kowalski");

        mockMvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCurrencyRate_IncorrectCurrencyFormat_ThrowsException() throws Exception {
        CurrencyClientRequest request = new CurrencyClientRequest("XXXX", "Jan Kowalski");

        mockMvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testGetCurrencyRequests() throws Exception {
        CurrencyRequest request1 = new CurrencyRequest(
                "Jan Kowalski",
                "USD",
                LocalDateTime.now(),
                BigDecimal.valueOf(USD_RATE));
        CurrencyRequest request2 = new CurrencyRequest(
                "Krystyna Stanko",
                "EUR",
                LocalDateTime.now(),
                BigDecimal.valueOf(EUR_RATE));
        List<CurrencyRequest> requests = Arrays.asList(request1, request2);
        repository.saveAll(requests);

        mockMvc.perform(get("/currencies/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jan Kowalski"))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].value").value(USD_RATE))
                .andExpect(jsonPath("$[1].name").value("Krystyna Stanko"))
                .andExpect(jsonPath("$[1].currency").value("EUR"))
                .andExpect(jsonPath("$[1].value").value(EUR_RATE));
    }

    @Test
    void testGetCurrencyCodes() throws Exception {
        List<String> currencyCodes = Arrays.asList("USD", "EUR", "GBP");

        MvcResult result = mockMvc.perform(get("/currencies/codes"))
                .andExpect(status().isOk()).andReturn();
        List<String> parsedResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertTrue(parsedResponse.containsAll(currencyCodes), "Response doesn't contain expected currencies codes");
    }
}