package com.umbrella.currencyRate.service.impl;

import com.umbrella.currencyRate.config.JwtTokenUtil;
import com.umbrella.currencyRate.enums.ResponseCodes;
import com.umbrella.currencyRate.model.ApiResponse;
import com.umbrella.currencyRate.entity.CurrencyRate;
import com.umbrella.currencyRate.model.JwtRequest;
import com.umbrella.currencyRate.repository.CurrencyRateRepository;
import com.umbrella.currencyRate.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    @Value("${cbar.url}")
    private String url;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${auth.username}")
    private String username;

    private final CurrencyRateRepository currencyRateRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @Override
    public ApiResponse getCurrencyRatesByDate(Date date) throws ParserConfigurationException, IOException, SAXException {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<CurrencyRate> currencyRates = currencyRateRepository.findAllByRateDate(date);
            if (currencyRates.size() > 0) {
                apiResponse.setCode(ResponseCodes.DATA_FOUND);
                apiResponse.setResponse("Information has already been added to the table for this date");
            } else {
                ResponseEntity<String> response = createRestTemplate(date);
                if (response.getStatusCode() == HttpStatus.OK) {
                    apiResponse.setCode(ResponseCodes.SUCCESS);
                    parseXmlResponse(response.getBody(), date);
                } else {
                    apiResponse.setCode(ResponseCodes.INTERNAL_ERROR);
                    apiResponse.setResponse(response.getBody());
                }
            }
        } catch (Exception ex) {
            apiResponse.setCode(ResponseCodes.UNKNOWN_ERROR);
            apiResponse.setResponse(ex.getLocalizedMessage());
        }
        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse deleteCurrencyRatesByDate(Date date) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            currencyRateRepository.deleteByRateDate(date);
            apiResponse.setCode(ResponseCodes.SUCCESS);
        } catch (Exception ex) {
            apiResponse.setCode(ResponseCodes.UNKNOWN_ERROR);
            apiResponse.setResponse(ex.getLocalizedMessage());
        }
        return apiResponse;
    }

    @Override
    public ApiResponse getCurrencyRates(Date date, String code) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<CurrencyRate> currencyRates = currencyRateRepository.findCurrencyRate(date, code);
            apiResponse.setCode(ResponseCodes.SUCCESS);
            apiResponse.setResponse(currencyRates);
        } catch (Exception ex) {
            apiResponse.setCode(ResponseCodes.UNKNOWN_ERROR);
            apiResponse.setResponse(ex.getLocalizedMessage());
        }
        return apiResponse;
    }

    @Override
    public ApiResponse getToken(JwtRequest jwtRequest) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(jwtRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);
            apiResponse.setResponse(token);
            apiResponse.setCode(ResponseCodes.SUCCESS);
        } catch (Exception ex) {
            apiResponse.setResponse(ex.getLocalizedMessage());
            apiResponse.setCode(ResponseCodes.UNKNOWN_ERROR);
        }
        return apiResponse;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new Exception("Exception authenticate: ", e);
        }
    }

    private ResponseEntity<String> createRestTemplate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String url_get = url + dateFormat.format(date) + ".xml";
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        return restTemplate.exchange(url_get, HttpMethod.GET, request, String.class);
    }

    private void parseXmlResponse(String body, Date date) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(body));
        Document document = builder.parse(is);
        NodeList nodeList = document.getElementsByTagName("Valute");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                CurrencyRate currencyRate = new CurrencyRate();
                currencyRate.setCurrencyCode(element.getAttribute("Code"));
                currencyRate.setRateDate(date);
                currencyRate.setCurrencyName(element.getElementsByTagName("Name").item(0).getTextContent());
                currencyRate.setRateValue(Double.parseDouble(element.getElementsByTagName("Value").item(0).getTextContent()));
                currencyRateRepository.save(currencyRate);
            }
        }
    }
}
