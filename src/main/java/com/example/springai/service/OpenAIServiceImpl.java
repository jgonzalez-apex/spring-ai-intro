package com.example.springai.service;

import com.example.springai.model.record.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel chatModel;

    private final ObjectMapper objectMapper;

    @Value("classpath:templates/capital-prompt.st")
    private Resource capitalPrompt;

    @Value("classpath:templates/capital-prompt-detailed.st")
    private Resource capitalPromptDetailed;

    @Value("classpath:templates/weather-prompt.st")
    private Resource weatherPrompt;

    /**
     * Generates an answer based on the provided question by interacting with a chat model.
     *
     * @param question the question to be answered, encapsulated in a {@link Question} object
     * @return the generated answer encapsulated in an {@link Answer} object
     */
    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    /**
     * Retrieves the capital of a given state or country by interacting with a chat model.
     *
     * @param request the {@link CapitalRequest} object containing the name of the state or country
     * @return the answer encapsulated in an {@link Answer} object, representing the capital
     */
    @Override
    public Answer getCapital(CapitalRequest request) {
        PromptTemplate promptTemplate = new PromptTemplate(capitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", request.stateOrCountry()));

        ChatResponse response = chatModel.call(prompt);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getResult().getOutput().getText());
            return new Answer(jsonNode.get("answer").asText());
        } catch (Exception e) { log.error("Error parsing JSON: ", e);}

        return new Answer("Operation failed.");
    }

    /**
     * Retrieves detailed information about the capital of a given state or country
     * by interacting with a chat model and using a specific prompt template.
     *
     * @param request the {@link CapitalRequest} object containing the name of the state or country
     * @return the detailed answer encapsulated in an {@link Answer} object, representing information about the capital
     */
    @Override
    public Answer getCapitalDetailed(CapitalRequest request) {
        PromptTemplate promptTemplate = new PromptTemplate(capitalPromptDetailed);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", request.stateOrCountry()));

        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public WeatherResponse getWeather(WeatherRequest request) {
        BeanOutputConverter<WeatherResponse> converter = new BeanOutputConverter<>(WeatherResponse.class);
        String format = converter.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(weatherPrompt);
        Prompt prompt = promptTemplate.create(Map.of("city", request.cityName(), "format", format));

        ChatResponse response = chatModel.call(prompt);

        return converter.convert(Optional.ofNullable(response.getResult().getOutput().getText()).orElse(""));
    }
}
