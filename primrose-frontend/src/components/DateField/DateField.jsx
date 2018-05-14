import React from "react";
import { DatePicker } from "material-ui-pickers";
import { Field } from "react-final-form";

import withStyles from "@material-ui/core/styles/withStyles";


export const style = theme => ({

});

const DateField = props => (
  <Field allowNull {...props}>
    {({ input, meta, ...rest }) => (
      <DatePicker
        id={input.name}
        {...input}
        {...rest}
      />
    )}
  </Field>
);

export default withStyles(style)(DateField);