import React, { useCallback, useState } from "react";

import { useNavigate } from "react-router-dom";
import { Box, Button, Typography } from "@mui/material";
import { Questionnaire, UserQuestionnaire } from "../../backendapi/types";
import backendClient from "../../backendApiClient";
import { AlertsView } from "../shared/AlertsView";

async function createUserQuestionnaire(): Promise<UserQuestionnaire> {
  const defaultQuestionnaire: Questionnaire | null = await backendClient.getDefaultQuestionnaire();

  if (!defaultQuestionnaire) {
    throw new Error("No default questionnaire found!");
  }
  return await backendClient.createUserQuestionnaire(defaultQuestionnaire.id);
}

const LandingView: React.FC<{}> = () => {
  const [errors, setErrors] = useState<string[]>([]);
  const navigate = useNavigate();

  const onStart = useCallback(async () => {
    try {
      const userQuestionnaire: UserQuestionnaire = await createUserQuestionnaire();
      navigate(`/questionnaire/${userQuestionnaire.id}`);
    } catch (e) {
      setErrors(["Cannot start personality test right now. Please come back later!"]);
    }
  }, [setErrors, navigate]);

  return (
    <Box justifyItems={"center"} display={"flex"} flexDirection={"column"} justifyContent={"center"}>
      <AlertsView messages={errors} severity={"error"} />
      <Typography variant={"h5"}>Are you an introvert or an extrovert?</Typography>
      <Button onClick={onStart}>Take the test!</Button>
    </Box>
  );
};

export default LandingView;
