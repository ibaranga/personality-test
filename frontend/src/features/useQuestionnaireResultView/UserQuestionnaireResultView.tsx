import React, { useEffect, useState } from "react";
import { Box, Button, Container, LinearProgress, Typography } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import { UserQuestionnaire, UserQuestionnaireResult } from "../../backendapi/types";
import backendApiClient from "../../backendApiClient";
import { AlertsView } from "../shared/AlertsView";

async function getUserQuestionnaireResult(id: string | undefined): Promise<UserQuestionnaireResult> {
  const userQuestionnaireId = Number(id);
  if (!Number.isInteger(userQuestionnaireId)) {
    throw new Error(`Invalid ID: ${id}`);
  }
  return await backendApiClient.getUserQuestionnaireResult(userQuestionnaireId);
}

const UserQuestionnaireResultView: React.FC = () => {
  const params = useParams();
  const navigate = useNavigate();

  const [errors, setErrors] = useState<string[]>([]);
  const [userQuestionnaireResult, setUserQuestionnaireResult] = useState<UserQuestionnaireResult | null>(null);
  const onError = (error: Error) => setErrors((prev) => prev.concat(error.message));

  useEffect(() => {
    getUserQuestionnaireResult(params.id).then(setUserQuestionnaireResult, onError);
  }, [params.id]);

  if (errors.length) {
    return <AlertsView messages={errors} severity="error" />;
  }

  if (!userQuestionnaireResult) {
    return <LinearProgress variant="indeterminate" />;
  }

  console.log(userQuestionnaireResult);
  return (
    <Box display={"flex"} justifyContent={"stretch"} flexDirection={"column"} justifyItems={"center"}>
      <Box>
        <Typography align={"center"} variant={"h5"}>
          You are an {userQuestionnaireResult.result}
        </Typography>
      </Box>

      <Button onClick={() => navigate("/")}>Home</Button>
    </Box>
  );
};

export default UserQuestionnaireResultView;
