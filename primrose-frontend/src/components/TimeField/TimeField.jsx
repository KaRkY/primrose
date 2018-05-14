import React from "react";
import { TimePicker } from "material-ui-pickers";
import { Field } from "react-final-form";

import withStyles from "@material-ui/core/styles/withStyles";

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