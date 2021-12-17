export interface Questionnaire {
  id: number;
  name: string;
  items: QuestionnaireItem[];
}

export interface QuestionnaireItem {
  id: number;

  value: string;

  index: number;

  options: QuestionnaireItemOption[];
}

export interface QuestionnaireItemOption {
  id: number;

  index: number;

  value: number;
}

export interface UserQuestionnaire {
  id: number;

  questionnaire: Questionnaire;

  completed: boolean;

  answers: UserQuestionnaireAnswer[];
}

export interface UserQuestionnaireResult {
  result: string;
}

export interface UserQuestionnaireAnswer {
  id?: number;

  userQuestionnaireId: number;

  questionnaireItemId: number;

  questionnaireItemOptionId: number;
}
