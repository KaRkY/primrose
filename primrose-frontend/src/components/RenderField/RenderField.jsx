import React from "react";
import { Field } from "react-final-form";

import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";


export const style = theme => ({

});

const RenderField = ({ children, ...restProps}) => (
  <Field {...restProps}>
    {({ input, meta, mapValue, ...rest }) => (
      <Typography {...rest}>{mapValue ? mapValue(input.value) : input.value}</Typography>
    )}
  </Field>
);

export default withStyles(style)(RenderField);