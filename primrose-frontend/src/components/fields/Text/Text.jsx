import React from "react";
import { Field } from "react-form";

import TextField from "@material-ui/core/TextField";

const Text = ({ validate, field, onChange, onBlur, helperText, ...props }) => (
  <Field validate={validate} field={field}>
    {({ value, error, warning, success, setValue, setTouched }) => (
      <TextField
        error={!!error}
        helperText={error || helperText}
        value={value || ""}
        onChange={e => {
          setValue(e.target.value);
          if (onChange) {
            onChange(e, e.target.value);
          }
        }}
        onBlur={e => {
          setTouched();
          if (onBlur) {
            onBlur(e);
          }
        }}
        {...props}
      />
    )}
  </Field>
);

export default Text;