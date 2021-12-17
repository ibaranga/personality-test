package com.ionutbaranga.ptapp.control;

import com.ionutbaranga.ptapp.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserQuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final UserQuestionnaireRepository userQuestionnaireRepository;

    public Optional<UserQuestionnaire> findById(Long userQuestionnaireId) {
        return userQuestionnaireRepository.findById(userQuestionnaireId);
    }

    public UserQuestionnaire createUserQuestionnaire(Long questionnaireId) {
        Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
                // TODO custom exception + exception mapper
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid questionnaireId"));

        UserQuestionnaire userQuestionnaire = UserQuestionnaire.builder()
                .questionnaire(questionnaire)
                .answers(List.of())
                .build();

        return userQuestionnaireRepository.save(userQuestionnaire);

    }

    public void updateUserQuestionnaire(Long id, UserQuestionnaire userQuestionnaireUpdate) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.findById(id)
                // TODO custom exception + exception mapper
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userQuestionnaire.isCompleted()) {
            // TODO custom exception + exception mapper
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot update a completed questionnaire");
        }

        userQuestionnaire.setAnswers(userQuestionnaireUpdate.getAnswers());
        userQuestionnaireRepository.save(userQuestionnaire);
    }

    public UserQuestionnaireResult getUserQuestionnaireResult(Long userQuestionnaireId) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.findById(userQuestionnaireId)
                // TODO custom exception + exception mapper
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<Long, QuestionnaireItem> itemsById = userQuestionnaire.getQuestionnaire().getItems().stream()
                .collect(Collectors.toMap(QuestionnaireItem::getId, Function.identity()));

        if (!userQuestionnaire.isCompleted()) {
            // TODO custom exception + exception mapper
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot calculate results of an incomplete questionnaire");
        }

        double introvertDiff = userQuestionnaire.getAnswers().stream().reduce(
                0.0,
                (diff, answer) -> {
                    QuestionnaireItemOption option = itemsById.get(answer.getQuestionnaireItemId())
                            .getOptions()
                            .stream()
                            .filter(o -> o.getId().equals(answer.getQuestionnaireItemOptionId()))
                            .findFirst()
                            .orElseThrow();
                    return diff + option.getIntrovertScore() - option.getExtrovertScore();
                }, Double::sum);

        return UserQuestionnaireResult
                .builder()
                .result(introvertDiff > 0 ? "Introvert" : "Extrovert")
                .build();

    }
}
