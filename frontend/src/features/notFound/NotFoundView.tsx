import React, { useCallback, useState } from "react";

import { useNavigate } from "react-router-dom";
import { Button, Container, Typography } from "@mui/material";
import { Questionnaire, UserQuestionnaire } from "../../backendapi/types";
import backendClient from "../../backendApiClient";
import { AlertsView } from "../shared/AlertsView";

const NotFoundView: React.FC<{}> = () => {
  return (
    <Container>
      <Typography>
        Hmm, this doesn't look like a real page. Try <a href="/">this one</a>
      </Typography>
    </Container>
  );
};

export default NotFoundView;
