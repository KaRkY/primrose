import React from "react";
import { Field } from "redux-form";

import MUICheckbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";

const renderField = ({
  input: { checked, value, ...input},
  label,
  meta: { touched, error },
  ...custom
}) => {
  const checkbox = <MUICheckbox
    checked={checked}
    {...input}
    {...custom}
  />;

  if(label) {
    return <FormControlLabel control={checkbox} label={label} />
  } else {
    return checkbox;
  }
};

const Checkbox = props => (
  <Field {...props} component={renderField} />
);

export default Checkbox;