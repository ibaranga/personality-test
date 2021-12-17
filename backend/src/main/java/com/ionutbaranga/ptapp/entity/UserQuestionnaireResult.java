package com.ionutbaranga.ptapp.entity;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserQuestionnaireResult {
    String result;
}
