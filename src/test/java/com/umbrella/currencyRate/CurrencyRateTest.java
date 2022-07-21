package com.umbrella.currencyRate;

import com.umbrella.currencyRate.enums.ResponseCodes;
import com.umbrella.currencyRate.model.ApiResponse;
import com.umbrella.currencyRate.model.JwtRequest;
import com.umbrella.currencyRate.service.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyRateTest {

    @Value("${auth.username}")
    private String username;

    @Value("${auth.password}")
    private String password;

    @Autowired
    CurrencyRateService currencyRateService;

    @Test
    void getCurrencyRates() throws Exception {
        ApiResponse response = currencyRateService.getCurrencyRates(new SimpleDateFormat("dd.MM.yyyy").parse("25.05.2022"), "XPT");
        assertThat("response:", response.getCode() == ResponseCodes.SUCCESS);
    }

    @Test
    void getCurrencyRatesByDate() throws Exception {
        ApiResponse response = currencyRateService.getCurrencyRatesByDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-22"));
        assertThat("response:", response.getCode() == ResponseCodes.SUCCESS);
    }

    @Test
    void deleteCurrencyRatesByDate() throws Exception {
        ApiResponse response = currencyRateService.deleteCurrencyRatesByDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-22"));
        assertThat("response:", response.getCode() == ResponseCodes.SUCCESS);
    }

    @Test
    void getToken() throws Exception {
        ApiResponse response = currencyRateService.getToken(new JwtRequest(username, password));
        assertThat("response:", response.getCode() == ResponseCodes.SUCCESS);
    }
}