import React, { useCallback, useEffect, useMemo, useState } from "react";
import { QuestionnaireItem, UserQuestionnaire, UserQuestionnaireAnswer } from "../../backendapi/types";
import { useNavigate, useParams } from "react-router-dom";
import { AppBar, Box, Button, Container, LinearProgress, List, Toolbar } from "@mui/material";
import backendApiClient from "../../backendApiClient";
import { AlertsView } from "../shared/AlertsView";
import { QuestionnaireAnswerView } from "./UserQuestionnaireAnswerView";
import keyBy from "lodash/keyBy";
import { unionBy } from "lodash";

import "./UserQuestionnaireView.css";

async function getUserQuestionnaire(id: string | undefined): Promise<UserQuestionnaire> {
  const userQuestionnaireId = Number(id);
  if (!Number.isInteger(userQuestionnaireId)) {
    throw new Error(`Invalid ID: ${id}`);
  }
  const userQuestionnaire = await backendApiClient.getUserQuestionnaire(userQuestionnaireId);
  if (!userQuestionnaire) {
    throw new Error(`User questionnaire not found for id=${id}`);
  }
  return userQuestionnaire;
}

const UserQuestionnaireView: React.FC = () => {
  const params = useParams();
  const navigate = useNavigate();

  const [userQuestionnaire, setUserQuestionnaire] = useState<UserQuestionnaire | null>(null);
  const [errors, setErrors] = useState<string[]>([]);
  const [infos, setInfos] = useState<string[]>([]);
  const [warnings, setWarnings] = useState<string[]>([]);
  const onError = (error: Error) => setErrors((prev) => prev.concat(error.message));

  useEffect(() => {
    getUserQuestionnaire(params.id).then(setUserQuestionnaire, onError);
  }, [params.id]);

  const answersByItemId = useMemo(
    () => keyBy(userQuestionnaire?.answers || [], "questionnaireItemId"),
    [userQuestionnaire?.answers]
  );

  const canSubmit = useMemo(
    () =>
      userQuestionnaire &&
      !userQuestionnaire.completed &&
      userQuestionnaire.questionnaire.items.length === userQuestionnaire.answers.length,
    [userQuestionnaire]
  );

  const onAnswerChange = useCallback(
    (item: QuestionnaireItem) => (questionnaireItemOptionId: number) =>
      setUserQuestionnaire((prev) => {
        if (!prev) {
          return prev;
        }
        const answer = { questionnaireItemId: item.id, userQuestionnaireId: prev.id, questionnaireItemOptionId };
        return { ...prev, answers: unionBy([answer], prev.answers, "questionnaireItemId") };
      }),
    [answersByItemId]
  );

  const onSubmit = useCallback(async () => {
    if (userQuestionnaire) {
      await backendApiClient.submitUserQuestionnaire(userQuestionnaire);
      getUserQuestionnaire(params.id).then(setUserQuestionnaire, onError);
    }
  }, [userQuestionnaire]);

  const onCheckResults = useCallback(async () => {
    if (userQuestionnaire) {
      navigate(`/questionnaire/${userQuestionnaire.id}/result`);
    }
  }, [userQuestionnaire]);

  if (errors.length) {
    return <AlertsView messages={errors} severity="error" />;
  }

  if (!userQuestionnaire) {
    return <LinearProgress variant="indeterminate" />;
  }

  const questionnaireItems = (
    <>
      {userQuestionnaire.questionnaire.items.map((item) => (
        <QuestionnaireAnswerView
          disabled={userQuestionnaire?.completed}
          key={item.id}
          item={item}
          answer={answersByItemId[item.id] ?? null}
          onChange={onAnswerChange(item)}
        />
      ))}
    </>
  );

  return (
    <Box display={"flex"} justifyContent={"stretch"} flexDirection={"column"} justifyItems={"center"}>
      <List>{questionnaireItems}</List>
      <Box>
        <Button disabled={!canSubmit} onClick={onSubmit}>
          Submit
        </Button>
        <Button disabled={!userQuestionnaire.completed} onClick={onCheckResults}>
          Check Results
        </Button>
      </Box>
      <AlertsView messages={warnings} severity="warning" />
      <AlertsView messages={infos} severity="info" />
    </Box>
  );
};

export default UserQuestionnaireView;
