package com.ionutbaranga.ptapp.boundary;

import com.ionutbaranga.ptapp.control.QuestionnaireRepository;
import com.ionutbaranga.ptapp.entity.Questionnaire;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questionnaires")
@RequiredArgsConstructor
@CrossOrigin
public class QuestionnaireController {
    private final QuestionnaireRepository questionnaireRepository;

    @GetMapping()
    public List<Questionnaire> findAll(Pageable pageable) {
        return questionnaireRepository.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public Optional<Questionnaire> findById(@PathVariable("id") Long id) {
        return questionnaireRepository.findById(id);
    }

}
