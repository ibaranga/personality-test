import React, { useCallback } from "react";
import { QuestionnaireItem, UserQuestionnaireAnswer } from "../../backendapi/types";
import { FormControl, FormControlLabel, FormLabel, ListItem, Radio, RadioGroup } from "@mui/material";

export const QuestionnaireAnswerView: React.FC<{
  item: QuestionnaireItem;
  onChange: (optionId: number) => void;
  answer: UserQuestionnaireAnswer | null;
  disabled: boolean;
}> = ({ disabled, item, answer, onChange }) => {
  const onOptionClick = useCallback((optionId: number) => () => onChange(optionId), [onChange]);

  return (
    <ListItem>
      <FormControl component="fieldset">
        <FormLabel component="legend">{item.value}</FormLabel>
        <RadioGroup aria-label="gender" defaultValue="female" name="radio-buttons-group">
          {item.options.map((option) => (
            <FormControlLabel
              onClick={onOptionClick(option.id)}
              checked={answer?.questionnaireItemOptionId === option.id}
              key={option.id}
              disabled={disabled}
              value={option.id}
              control={<Radio />}
              label={option.value}
            />
          ))}
        </RadioGroup>
      </FormControl>
    </ListItem>
  );
};
