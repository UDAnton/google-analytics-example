package com.github.udanton.ga.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Currency {
    @JsonProperty(value = "cc")
    private String currency;
    private Long rate;
    @JsonProperty(value = "exchangedate")
    private String exchangeDate;
}
