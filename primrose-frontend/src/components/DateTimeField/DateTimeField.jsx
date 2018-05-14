import React from "react";
import { DateTimePicker } from "material-ui-pickers";
import { Field } from "react-final-form";

import withStyles from "@material-ui/core/styles/withStyles";

export const style = theme => ({

});

const TimeField = props => (
  <Field allowNull {...props}>
    {({ input, meta, ...rest }) => (
      <DateTimePicker
        id={input.name}
        error={meta.touched && !!meta.error} 
        helperText={meta.touched && meta.error} 
        {...input}
        {...rest}
      />
    )}
  </Field>
);

export default withStyles(style)(TimeField);