import React from "react";
import { Alert, AlertColor, Container } from "@mui/material";

export const AlertsView: React.FC<{ messages: string[]; severity: AlertColor }> = ({ messages, severity }) => {
  if (!messages.length) {
    return null;
  }
  return (
    <Container>
      {messages.map((error) => (
        <Alert severity={severity}>{error}</Alert>
      ))}
    </Container>
  );
};
