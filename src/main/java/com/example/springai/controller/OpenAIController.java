package com.example.springai.controller;

import com.example.springai.model.record.*;
import com.example.springai.service.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/openai")
@AllArgsConstructor
public class OpenAIController {
    private final OpenAIService openAIService;

    @PostMapping(value = "/answer")
    public Answer getAnswer(@RequestBody Question request) {
        return openAIService.getAnswer(request);
    }

    @PostMapping(value = "/capital")
    public Answer getCapital(@RequestBody CapitalRequest request) {
        return openAIService.getCapital(request);
    }

    @PostMapping(value = "/capital-detailed")
    public Answer getCapitalDetailed(@RequestBody CapitalRequest request) {
        return openAIService.getCapitalDetailed(request);
    }

    @PostMapping(value = "/weather")
    public WeatherResponse getWeather(@RequestBody WeatherRequest request) {
        WeatherResponse response = openAIService.getWeather(request);
        log.info("Response: {}", response);
        return response;
    }
}
