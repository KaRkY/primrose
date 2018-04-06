import React from "react";
import { withStyles } from "material-ui/styles";
import MUITextField from "material-ui/TextField";
import { Field } from "react-final-form";


export const style = theme => ({

});

const TextField = ({ children, ...restProps}) => (
  <Field {...restProps}>
    {({ input: {onChange, onFocus, onBlur, ...restInput}, meta, ...rest }) => (
      <MUITextField 
        id={restInput.name}
        error={meta.touched && !!meta.error} 
        helperText={meta.touched && meta.error} 
        onChange={event => console.log("onChange") || onChange(event)}
        onFocus={event => console.log("onFocus") || onFocus(event)}
        onBlur={event => console.log("onBlur") || onBlur(event)}
        {...restInput}
        {...rest}
        children={children}/>
    )}
  </Field>
);

export default withStyles(style)(TextField);