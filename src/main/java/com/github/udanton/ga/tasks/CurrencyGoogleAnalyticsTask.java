package com.github.udanton.ga.tasks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.udanton.ga.config.ApplicationProperties;
import com.github.udanton.ga.tasks.dto.Currency;
import com.github.udanton.ga.tasks.dto.CurrencyGoogleAnalyticsPayload;
import com.github.udanton.ga.tasks.dto.GoogleAnalyticEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyGoogleAnalyticsTask {

    private static final String NBU_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    private static final String TEST_CLIENT_ID = "1";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;

    @Scheduled(cron = "0 * * * * ?")
    public void run() throws Exception {
        Optional<Currency> possibleCurrency = getUSDCurrency();
        if (possibleCurrency.isPresent()) {
            final Currency currency = possibleCurrency.get();
            log.info("Currency: {}", currency);
            sendToGoogleAnalytics(currency);
        } else {
            log.info("Currency isn't retrieved");
        }
    }

    private Optional<Currency> getUSDCurrency() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(NBU_URL, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            List<Currency> currencies = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
            return currencies.stream()
                .filter(currency -> Objects.equals(currency.getCurrency(), "USD"))
                .findFirst();
        }
        return Optional.empty();
    }

    private void sendToGoogleAnalytics(Currency currency) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        CurrencyGoogleAnalyticsPayload currencyGoogleAnalyticsPayload = buildPayload(currency, TEST_CLIENT_ID);
        HttpEntity<CurrencyGoogleAnalyticsPayload> request = new HttpEntity<>(currencyGoogleAnalyticsPayload, headers);

        String url = String.format("%s?api_secret=%s&measurement_id=%s", applicationProperties.getUrl(),
            applicationProperties.getApiSecret(), applicationProperties.getMeasurementId());
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        log.info("GA response: {}", response);
    }

    private static CurrencyGoogleAnalyticsPayload buildPayload(final Currency currency, final String clientId) {
        var payload = new CurrencyGoogleAnalyticsPayload();
        payload.setClientId(clientId);
        payload.setNonPersonalizedAds(true);
        payload.setEvents(List.of(getEventParams(currency)));
        return payload;
    }

    private static GoogleAnalyticEvent getEventParams(final Currency currency) {
        var params = new HashMap<String, Object>();
        params.put("currency", currency.getCurrency());
        params.put("currency_rate", String.valueOf(currency.getRate()));
        params.put("exchange_date", currency.getExchangeDate());
        return GoogleAnalyticEvent.builder()
            .name("usd_exchange_rates")
            .params(params)
            .build();
    }

}
