package com.example.web_market_cosmo_cats.exception;

import java.util.Map;

public record ParseResult(String detail, Map<String, String> fieldErrors) {
}
