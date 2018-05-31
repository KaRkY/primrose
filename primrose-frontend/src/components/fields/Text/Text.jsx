import React from "react";
import { Field } from "redux-form";

import TextField from "@material-ui/core/TextField";

const renderField = ({
  input,
  meta: { touched, error },
  helperText,
  ...custom
}) => (
    <TextField
      error={touched && !!error}
      helperText={(touched && error) || helperText}
      {...input}
      {...custom}
    />
  );

const Text = props => (
  <Field {...props} component={renderField} />
);

export default Text;