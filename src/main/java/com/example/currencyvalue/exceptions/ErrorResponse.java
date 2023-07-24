package com.example.currencyvalue.exceptions;

import java.util.List;

public record ErrorResponse(String message, List<String> description) {

}
