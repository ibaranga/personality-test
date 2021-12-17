package com.ionutbaranga.ptapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ionutbaranga.ptapp.control.QuestionnaireRepository;
import com.ionutbaranga.ptapp.entity.Questionnaire;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@RequiredArgsConstructor
@Component
@Profile("testData")
public class PtAppDataLoader {

    private final ObjectMapper mapper;
    private final QuestionnaireRepository questionnaireRepository;

    @PostConstruct
    public void init() throws Exception {
        File defaultPersonalityTestFile = new ClassPathResource("defaultQuestionnaire.json").getFile();
        Questionnaire defaultQuestionnaire = mapper.readValue(defaultPersonalityTestFile, Questionnaire.class);
        defaultQuestionnaire.getItems().forEach(item -> item.setQuestionnaire(defaultQuestionnaire));
        questionnaireRepository.save(defaultQuestionnaire);
    }

}
