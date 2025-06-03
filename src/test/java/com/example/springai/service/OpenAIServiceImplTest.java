package com.example.springai.service;

import com.example.springai.model.record.Question;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenAIServiceImplTest {

    @Autowired
    private OpenAIServiceImpl openAIService;

    @Test
    void testGetAnswerWithValidQuestion() {
        // Arrange
        String question = "What is Jakarta EE?";
        String answer = openAIService.getAnswer(new Question(question)).answer();

        assertThat(answer).isNotBlank();
        System.out.println(answer);
    }

}