package com.example.Currencies.controller;

import com.example.Currencies.service.ExcelService;
import com.example.Currencies.service.ExchangeRateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = "*")
public class ExchangeController {
    private final ExchangeRateService exchangeRateService;
    private final ExcelService excelService;

    public ExchangeController(ExchangeRateService exchangeRateService, ExcelService excelService) {
        this.exchangeRateService = exchangeRateService;
        this.excelService = excelService;
    }

    @GetMapping("/rate")
    public Map<String, BigDecimal> getExchangeRates() {
        return exchangeRateService.getExchangeRates();
    }

    @GetMapping("/convert")
    public BigDecimal convertCurrency(@RequestParam BigDecimal amount, @RequestParam String currency) {
        Map<String, BigDecimal> rates = exchangeRateService.getExchangeRates();
        BigDecimal rate = rates.getOrDefault(currency.toUpperCase(), BigDecimal.ZERO);
        return amount.multiply(rate);
    }

    @GetMapping("/download-excel")
    public void downloadExcel(HttpServletResponse response,
                              @RequestParam BigDecimal amount,
                              @RequestParam String currency) throws IOException {
        excelService.exportExchangeRates(response, amount, currency);
    }
}
