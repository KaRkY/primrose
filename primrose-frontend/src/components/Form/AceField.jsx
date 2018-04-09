import React from "react";
import { withStyles } from "material-ui/styles";
import AceEditor  from "react-ace";
import { Field } from "react-final-form";

import "brace/mode/javascript";
import "brace/theme/github";
import "brace/theme/monokai";


export const style = theme => ({

});

const TextField = ({ children, theme, ...restProps}) => (
  <Field {...restProps}>
    {({ input, meta, ...rest }) => (
      <AceEditor
        mode="javascript"
        theme={theme.palette.type === "light" ? "github" : "monokai"}
        id={input.name}
        width="100%"
        {...input}
        {...rest}/>
    )}
  </Field>
);

export default withStyles(style, { withTheme: true })(TextField);