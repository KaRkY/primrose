import React from "react";
import { withStyles } from "material-ui/styles";
import { DatePicker } from "material-ui-pickers";
import { Field } from "react-final-form";


export const style = theme => ({

});

const DateField = props => (
  <Field allowNull {...props}>
    {({ input, meta, ...rest }) => console.log(value) || (
      <DatePicker
        id={input.name}
        {...input}
        {...rest}
      />
    )}
  </Field>
);

export default withStyles(style)(DateField);