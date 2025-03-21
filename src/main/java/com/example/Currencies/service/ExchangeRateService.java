package com.example.Currencies.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {
    public Map<String, BigDecimal> getExchangeRates() {
        // Симуляция курса валют
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", BigDecimal.valueOf(38.50));
        rates.put("EUR", BigDecimal.valueOf(41.20));
        return rates;
    }
}
