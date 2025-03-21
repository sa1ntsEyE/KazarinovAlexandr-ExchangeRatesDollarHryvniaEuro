package com.example.Currencies.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExcelService {
    private final ExchangeRateService exchangeRateService;

    public ExcelService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public void exportExchangeRates(HttpServletResponse response, BigDecimal amount, String currency) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Exchange Rates");

        // Заголовки
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Валюта");
        headerRow.createCell(1).setCellValue("Курс продажу (UAH)");

        // Получаем курсы валют
        Map<String, BigDecimal> rates = exchangeRateService.getExchangeRates();
        int rowNum = 1;

        for (Map.Entry<String, BigDecimal> entry : rates.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().doubleValue());
        }

        // Добавляем информацию о транзакции
        Row transactionHeaderRow = sheet.createRow(rowNum++);
        transactionHeaderRow.createCell(0).setCellValue("Конвертация");

        Row transactionRow = sheet.createRow(rowNum++);
        transactionRow.createCell(0).setCellValue("Сумма:");
        transactionRow.createCell(1).setCellValue(amount.doubleValue());

        transactionRow = sheet.createRow(rowNum++);
        transactionRow.createCell(0).setCellValue("Валюта:");
        transactionRow.createCell(1).setCellValue(currency);

        // Вычисляем результат конвертации
        BigDecimal rate = rates.getOrDefault(currency.toUpperCase(), BigDecimal.ZERO);
        BigDecimal convertedAmount = amount.multiply(rate);

        transactionRow = sheet.createRow(rowNum++);
        transactionRow.createCell(0).setCellValue("Результат в UAH:");
        transactionRow.createCell(1).setCellValue(convertedAmount.doubleValue());

        // Устанавливаем заголовки ответа и отправляем файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=exchange_rates.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
