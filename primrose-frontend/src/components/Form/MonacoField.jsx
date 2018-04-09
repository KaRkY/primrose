import React from "react";
import compose from "recompose/compose";
import lifecycle from "recompose/lifecycle";
import { withStyles } from "material-ui/styles";
import MonacoEditor from "react-monaco-editor";
import { Field } from "react-final-form";


export const style = theme => ({

});

const enhance = compose(
  withStyles(style),
  lifecycle({
    componentDidMount() {
      this.setState({ mounted: true });
    }
  }),
);

const MonacoField = ({ mounted, ...props }) => (
  mounted ?
    <Field {...props}>
      {({ input, meta, ...rest }) => console.log(input, props) || (
        <MonacoEditor
          id={input.name}
          language="javascript"
          theme="vs-dark"
          {...input}
          {...rest}
        />
      )}
    </Field> :
    null
);

export default enhance(MonacoField);