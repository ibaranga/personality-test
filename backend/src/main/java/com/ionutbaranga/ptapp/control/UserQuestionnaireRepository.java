package com.ionutbaranga.ptapp.control;

import com.ionutbaranga.ptapp.entity.UserQuestionnaire;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserQuestionnaireRepository extends PagingAndSortingRepository<UserQuestionnaire, Long> {
}
