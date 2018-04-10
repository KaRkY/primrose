import React from "react";
import { withStyles } from "material-ui/styles";
import MUITextField from "material-ui/TextField";
import { Field } from "react-final-form";


export const style = theme => ({

});

const TextField = ({ children, ...restProps}) => (
  <Field {...restProps}>
    {({ input, meta, ...rest }) => (
      <MUITextField 
        id={input.name}
        error={meta.touched && !!meta.error} 
        helperText={meta.touched && meta.error}
        {...input}
        {...rest}
        children={children}
        />
    )}
  </Field>
);

export default withStyles(style)(TextField);