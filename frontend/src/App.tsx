import React from "react";
import { BrowserRouter, Route, Routes, useParams } from "react-router-dom";
import LandingView from "./features/landingScreen/LandingView";
import UserQuestionnaireView from "./features/userQuestionnaire/UserQuestionnaireView";
import UserQuestionnaireResultView from "./features/useQuestionnaireResultView/UserQuestionnaireResultView";
import { AppBar, Box, Container, CssBaseline, Toolbar, Typography } from "@mui/material";
import NotFoundView from "./features/notFound/NotFoundView";

const App: React.FC<{}> = () => {
  const params = useParams();
  console.log(params);
  return (
    <BrowserRouter>
      <React.Fragment>
        <CssBaseline />
        <AppBar variant={"elevation"}>
          <Toolbar>
            <Typography>Personality Test</Typography>
          </Toolbar>
        </AppBar>
        <Toolbar />
        <Box display={"flex"} justifyContent={"center"} justifyItems={"center"}>
          <Routes>
            <Route path="/" element={<LandingView />} />
            <Route path="/questionnaire/:id" element={<UserQuestionnaireView />} />
            <Route path="/questionnaire/:id/result" element={<UserQuestionnaireResultView />} />
            <Route path="*" element={<NotFoundView />} />
          </Routes>
        </Box>
      </React.Fragment>
    </BrowserRouter>
  );
};

export default App;
