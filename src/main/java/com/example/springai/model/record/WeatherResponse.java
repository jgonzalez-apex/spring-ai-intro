package com.example.springai.model.record;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record WeatherResponse(@JsonPropertyDescription("This is the current weather") String weather,
                              @JsonPropertyDescription("This is tomorrow's weather")  String tomorrowWeather)
{ }
