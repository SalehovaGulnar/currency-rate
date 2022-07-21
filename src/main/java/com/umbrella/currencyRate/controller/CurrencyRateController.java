package com.umbrella.currencyRate.controller;

import com.umbrella.currencyRate.model.ApiResponse;
import com.umbrella.currencyRate.model.JwtRequest;
import com.umbrella.currencyRate.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CurrencyRateController {

    @GetMapping("/currency-rates")
    public ApiResponse getCurrencyRates(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam(required = false) String code) {
        return currencyRateService.getCurrencyRates(date, code);
    }

    private final CurrencyRateService currencyRateService;

    @GetMapping("/cbar-currency-rates/{date}")
    public ApiResponse getCurrencyRatesByDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ParserConfigurationException, IOException, SAXException {
        return currencyRateService.getCurrencyRatesByDate(date);
    }

    @DeleteMapping("/cbar-currency-rates/{date}")
    public ApiResponse deleteCurrencyRatesByDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return currencyRateService.deleteCurrencyRatesByDate(date);
    }

    @PostMapping("currency-rates/token")
    public ApiResponse getToken(@RequestBody JwtRequest jwtRequest) {
        return currencyRateService.getToken(jwtRequest);
    }
}
