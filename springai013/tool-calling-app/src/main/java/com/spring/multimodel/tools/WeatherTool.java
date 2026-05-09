package com.spring.multimodel.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WeatherTool {

    private final RestClient restClient;

    public WeatherTool(RestClient restClient) {
        this.restClient = restClient;
    }

    @Value("${app.weather.api-key}")
    private String weatherApiKey;

    @Tool(description = "get weather information of given city.")
    public String getWeather(@ToolParam(description = "city of which we want to get weather information") String city) {
        //external api use karni hai, to get current weather information
        System.out.println("get weather information of given city.");
        Map<String, Object> response = restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/current.json")
                                .queryParam("key", weatherApiKey)
                                .queryParam("q", city)
                                .build()
                )
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});

        if (response == null) {
            return "{}";
        }

        return response.toString();

    }
}
