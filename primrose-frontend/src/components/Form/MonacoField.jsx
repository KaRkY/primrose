import React from "react";
import { withStyles } from "material-ui/styles";
import MonacoEditor from "react-monaco-editor";
import { Field } from "react-final-form";


export const style = theme => ({

});

const MonacoField = props => (
  <Field {...props}>
    {({ input: { onBlur, onFocus, onChange, restInput}, meta, ...rest }) => (
      <MonacoEditor
        language="javascript"
        theme="vs-dark"
        onChange={value => onChange(value)}
        {...restInput}
        {...rest}
      />
    )}
  </Field>
);

export default withStyles(style)(MonacoField);