package com.github.udanton.ga.tasks.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyGoogleAnalyticsPayload {
    @JsonProperty(value = "client_id")
    private String clientId;
    @JsonProperty(value = "non_personalized_ads")
    private Boolean nonPersonalizedAds;
    private List<GoogleAnalyticEvent> events;
}
