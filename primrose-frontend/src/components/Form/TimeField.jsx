import React from "react";
import { withStyles } from "material-ui/styles";
import { TimePicker } from "material-ui-pickers";
import { Field } from "react-final-form";


export const style = theme => ({

});

const TimeField = props => (
  <Field allowNull {...props}>
    {({ input, meta, ...rest }) => (
      <TimePicker
        id={input.name}
        {...input}
        {...rest}
      />
    )}
  </Field>
);

export default withStyles(style)(TimeField);