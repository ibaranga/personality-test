import {
  Questionnaire,
  QuestionnaireItem,
  UserQuestionnaire,
  UserQuestionnaireAnswer,
  UserQuestionnaireResult,
} from "./types";

export class BackendApiClient {
  constructor(private readonly baseUrl: string = "http://localhost:9090") {}

  public async getDefaultQuestionnaire(): Promise<Questionnaire | null> {
    const url = `${this.baseUrl}/questionnaires`;
    const response = await fetch(url);
    if (response.status !== 200) {
      throw new Error(response.statusText);
    }
    const questionnaires = (await response.json()) as Questionnaire[];
    return questionnaires[0] ?? null;
  }

  public async getUserQuestionnaireResult(userQuestionnaireId: number): Promise<UserQuestionnaireResult> {
    const url = `${this.baseUrl}/userQuestionnaires/${userQuestionnaireId}/result`;
    const response = await fetch(url);
    if (response.status !== 200) {
      throw new Error(response.statusText);
    }
    return (await response.json()) as UserQuestionnaireResult;
  }

  public async createUserQuestionnaire(questionnaireId: number): Promise<UserQuestionnaire> {
    const url = `${this.baseUrl}/userQuestionnaires`;
    const response = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ questionnaireId }),
    });
    if (response.status !== 200) {
      throw new Error(response.statusText);
    }
    return (await response.json()) as UserQuestionnaire;
  }

  public async getUserQuestionnaire(id: number): Promise<UserQuestionnaire | null> {
    const url = `${this.baseUrl}/userQuestionnaires/${id}`;
    const response = await fetch(url);
    return (await response.json()) as UserQuestionnaire;
  }

  public async submitUserQuestionnaire(userQuestionnaire: UserQuestionnaire): Promise<void> {
    const url = `${this.baseUrl}/userQuestionnaires/${userQuestionnaire.id}`;
    await fetch(url, {
      method: "POST",
      body: JSON.stringify(userQuestionnaire),
      headers: { "Content-Type": "application/json" },
    });
  }
}
