package com.ionutbaranga.ptapp.control;

import com.ionutbaranga.ptapp.entity.Questionnaire;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionnaireRepository extends PagingAndSortingRepository<Questionnaire, Long> {
}
