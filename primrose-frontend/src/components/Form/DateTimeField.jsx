import React from "react";
import { withStyles } from "material-ui/styles";
import { DateTimePicker } from "material-ui-pickers";
import { Field } from "react-final-form";


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