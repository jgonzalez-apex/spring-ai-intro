package com.example.springai.service;

import com.example.springai.model.record.*;

public interface OpenAIService {
    Answer getAnswer(Question question);

    Answer getCapital(CapitalRequest request);

    Answer getCapitalDetailed(CapitalRequest request);

    WeatherResponse getWeather(WeatherRequest request);
}
