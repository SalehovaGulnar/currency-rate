package com.umbrella.currencyRate.service;

import com.umbrella.currencyRate.model.ApiResponse;
import com.umbrella.currencyRate.model.JwtRequest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

public interface CurrencyRateService {

    ApiResponse getCurrencyRatesByDate(Date date) throws ParserConfigurationException, IOException, SAXException;

    ApiResponse deleteCurrencyRatesByDate(Date date);

    ApiResponse getCurrencyRates(Date date, String code);

    ApiResponse getToken(JwtRequest jwtRequest);
}
