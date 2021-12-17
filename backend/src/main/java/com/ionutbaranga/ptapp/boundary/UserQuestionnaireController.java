package com.ionutbaranga.ptapp.boundary;

import com.ionutbaranga.ptapp.control.UserQuestionnaireService;
import com.ionutbaranga.ptapp.entity.UserQuestionnaire;
import com.ionutbaranga.ptapp.entity.UserQuestionnaireResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/userQuestionnaires")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class UserQuestionnaireController {
    private final UserQuestionnaireService userQuestionnaireService;

    @PostMapping
    public UserQuestionnaire createUserQuestionnaire(@RequestBody Map<String, Long> request) {
        return userQuestionnaireService.createUserQuestionnaire(request.get("questionnaireId"));

    }

    @GetMapping("/{id}")
    private Optional<UserQuestionnaire> findById(@PathVariable("id") Long id) {
        return userQuestionnaireService.findById(id);
    }

    @GetMapping("/{id}/result")
    private UserQuestionnaireResult getResult(@PathVariable("id") Long id) {
        return userQuestionnaireService.getUserQuestionnaireResult(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateUserQuestionnaire(@PathVariable("id") Long id, @RequestBody UserQuestionnaire userQuestionnaire) {
        userQuestionnaireService.updateUserQuestionnaire(id, userQuestionnaire);
        return ResponseEntity.noContent().build();
    }


}
