package com.github.udanton.ga.tasks.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class GoogleAnalyticEvent {
    private String name;
    private Map<String, Object> params;
}
