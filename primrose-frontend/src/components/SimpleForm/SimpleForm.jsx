import React from "react";
import renderProp from "../../util/renderProp";
import { Form } from "react-final-form";

import compose from "recompose/compose";

import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import withStyles from "@material-ui/core/styles/withStyles";

export const style = theme => ({
  buttons: {
    marginTop: 3 * theme.spacing.unit,
  },
});

const enhance = compose(
  withStyles(style),
);

const SimpleForm = ({
  resetButtonText = "Reset",
  submitButtonText = "Submit",
  ...props
}) => (
  <Form
    {...props}
    render={formProps => (
      <form onSubmit={formProps.handleSubmit} onReset={() => formProps.form.reset()}>
        {renderProp(props, formProps)}
        <Grid className={props.classes.buttons} container spacing={16}>
          <Grid item>
            <Button variant="raised" type="reset" disabled={formProps.submitting || formProps.pristine}>{resetButtonText}</Button>
          </Grid>
          <Grid item>
            <Button variant="raised" color="secondary" type="submit" disabled={formProps.submitting || formProps.pristine}>{submitButtonText}</Button>
          </Grid>
        </Grid>
      </form>
    )} />
);

export default enhance(SimpleForm);