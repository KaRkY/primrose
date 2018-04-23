import React from "react";
import { withStyles } from "material-ui/styles";
import { Field } from "react-final-form";
import Typography from "material-ui/Typography";


export const style = theme => ({

});

const TextField = ({ children, ...restProps}) => (
  <Field {...restProps}>
    {({ input, meta, mapValue, ...rest }) => (
      <Typography {...rest}>{mapValue ? mapValue(input.value) : input.value}</Typography>
    )}
  </Field>
);

export default withStyles(style)(TextField);