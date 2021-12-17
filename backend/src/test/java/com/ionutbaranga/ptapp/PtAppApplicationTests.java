package com.ionutbaranga.ptapp;

import com.ionutbaranga.ptapp.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=testData")
class PtAppApplicationTests {

    @Autowired
    TestRestTemplate rest;

    @Test
    void happyFlowEndToEndScenarioWithDefaultPersonalityTest() {
        // GIVEN
        Questionnaire questionnaire = getDefaultQuestionnaire();
        UserQuestionnaire userQuestionnaire = createUserQuestionnaire(questionnaire.getId());

        // WHEN
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 1);
        // THEN
        assertThat(userQuestionnaire.getAnswers()).hasSize(1);

        // WHEN
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 2);
        // THEN
        assertThat(userQuestionnaire.getAnswers()).hasSize(2);

        // WHEN
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 3);
        // THEN
        assertThat(userQuestionnaire.getAnswers()).hasSize(3);

        // WHEN
        UserQuestionnaireResult result = getUserQuestionnaireResultResult(userQuestionnaire.getId());
        // THEN
        assertThat(result.getResult()).isEqualTo("Extrovert");
    }

    @Test
    void shouldNotAllowGettingTheResultOfAnIncompleteQuestionnaire() {
        // GIVEN
        Questionnaire questionnaire = getDefaultQuestionnaire();
        UserQuestionnaire userQuestionnaire = createUserQuestionnaire(questionnaire.getId());
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 1);
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 2);

        ResponseEntity<UserQuestionnaireResult> resultResponse = getUserQuestionnaireResultResultResponse(userQuestionnaire.getId());
        assertThat(resultResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotAllowUpdatingACompletedQuestionnaire() {
        // GIVEN
        Questionnaire questionnaire = getDefaultQuestionnaire();
        UserQuestionnaire userQuestionnaire = createUserQuestionnaire(questionnaire.getId());
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 1);
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 2);
        userQuestionnaire = answerQuestionnaireItem(userQuestionnaire, 3);

        // WHEN
        ResponseEntity<Void> response = answerQuestionnaireItemResponse(userQuestionnaire, 2);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotAllowCreatingAUserQuestionnaireWithAnInvalidQuestionnaireId() {
        // WHEN
        ResponseEntity<UserQuestionnaire> response = createUserQuestionnaireResponse(2312321L);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    UserQuestionnaireResult getUserQuestionnaireResultResult(Long userQuestionnaireId) {
        ResponseEntity<UserQuestionnaireResult> response = getUserQuestionnaireResultResultResponse(userQuestionnaireId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody();

    }

    ResponseEntity<UserQuestionnaireResult> getUserQuestionnaireResultResultResponse(Long userQuestionnaireId) {
        return rest.getForEntity(
                "/userQuestionnaires/{id}/result",
                UserQuestionnaireResult.class,
                Map.of("id", userQuestionnaireId));

    }

    UserQuestionnaire answerQuestionnaireItem(UserQuestionnaire userQuestionnaire, int itemIndex) {
        ResponseEntity<Void> responseEntity = answerQuestionnaireItemResponse(userQuestionnaire, itemIndex);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<UserQuestionnaire> updatedUserQuestionnaireEntity = rest.getForEntity(
                "/userQuestionnaires/{id}",
                UserQuestionnaire.class,
                Map.of("id", userQuestionnaire.getId())
        );

        assertThat(updatedUserQuestionnaireEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(updatedUserQuestionnaireEntity.getBody()).isNotNull();
        return updatedUserQuestionnaireEntity.getBody();
    }

    ResponseEntity<Void> answerQuestionnaireItemResponse(UserQuestionnaire userQuestionnaire, int itemIndex) {
        QuestionnaireItem nextItem = userQuestionnaire.getQuestionnaire().getItems()
                .stream()
                .filter(item -> item.getIndex() == itemIndex)
                .findFirst()
                .orElseThrow();

        UserQuestionnaireAnswer nextAnswer = UserQuestionnaireAnswer
                .builder()
                .userQuestionnaireId(userQuestionnaire.getId())
                .questionnaireItemId(nextItem.getId())
                .questionnaireItemOptionId(nextItem.getOptions().iterator().next().getId())
                .build();

        List<UserQuestionnaireAnswer> answers = Stream.concat(
                        userQuestionnaire.getAnswers().stream(),
                        Stream.of(nextAnswer))
                .toList();


        return rest.postForEntity(
                "/userQuestionnaires/{id}",
                userQuestionnaire.toBuilder().answers(answers).build(),
                Void.class,
                Map.of("id", userQuestionnaire.getId()));

    }

    Questionnaire getDefaultQuestionnaire() {
        ResponseEntity<List<Questionnaire>> response = rest.exchange(
                "/questionnaires",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody()).isNotEmpty();

        return response.getBody().get(0);
    }

    UserQuestionnaire createUserQuestionnaire(Long questionnaireId) {
        ResponseEntity<UserQuestionnaire> response = createUserQuestionnaireResponse(questionnaireId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        return response.getBody();
    }

    ResponseEntity<UserQuestionnaire> createUserQuestionnaireResponse(Long questionnaireId) {

        return rest.postForEntity(
                "/userQuestionnaires",
                Map.of("questionnaireId", questionnaireId),
                UserQuestionnaire.class);
    }

}
