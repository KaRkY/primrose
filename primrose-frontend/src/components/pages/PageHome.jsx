import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Input, { InputLabel } from "material-ui/Input";
import Button from "material-ui/Button";
import { FormControl, FormHelperText } from "material-ui/Form";
import Grid from "material-ui/Grid";
import { Form, Field } from "react-final-form";

const contentStyle = theme => ({
  container: {
    display: "flex",
    flexWrap: "wrap",
  },
  formControl: {
    margin: theme.spacing.unit,
  },
});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = ({ classes }) => (
  <Form
    initialValues={{
      firstName: "",
      lastName: "",
    }}
    onSubmit={(values) => new Promise(resolve => setTimeout(() => resolve(), 2000))}
    validate={values => {
      // same as above, but feel free to move this into a class method now.
      let errors = {};
      if (!values.firstName) {
        errors.firstName = "Required";
      }
      if (!values.lastName) {
        errors.lastName = "Required";
      }
      return errors;
    }}
    render={({
      handleSubmit,
      reset,
      submitting,
      pristine,
      values
    }) => (
        <form onSubmit={handleSubmit} onReset={reset}>
          <Grid container>

            <Grid item xs={12} md={6}>
              <Field name="firstName">
                {({ input, meta }) => (
                  <FormControl error={meta.touched && meta.error ? true : false} fullWidth>
                    <InputLabel htmlFor={input.name}>First name</InputLabel>
                    <Input id={input.name} type="text" {...input} />
                    {meta.touched && meta.error && <FormHelperText>{meta.error}</FormHelperText>}
                  </FormControl>
                )}
              </Field>
            </Grid>

            <Grid item xs={12} md={6}>
              <Field name="lastName">
                {({ input, meta }) => (
                  <FormControl error={meta.touched && meta.error ? true : false} fullWidth>
                    <InputLabel htmlFor={input.name}>Last name</InputLabel>
                    <Input id={input.name} type="text" {...input} />
                    {meta.touched && meta.error && <FormHelperText>{meta.error}</FormHelperText>}
                  </FormControl>
                )}
              </Field>
            </Grid>

            <Grid item xs={12} md={6} container>
              <Grid item>
                <FormControl>
                  <Button variant="raised" color="secondary" type="submit" disabled={submitting}>Submit</Button>
                </FormControl>
              </Grid>
              <Grid item>
                <FormControl>
                  <Button variant="raised" type="reset" disabled={submitting}>Reset</Button>
                </FormControl>
              </Grid>
            </Grid>
          </Grid>
        </form>
      )} />
);

export default enhance(Content);