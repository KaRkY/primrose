import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Input, { InputLabel } from "material-ui/Input";
import Button from "material-ui/Button";
import { FormControl, FormHelperText } from "material-ui/Form";
import Grid from "material-ui/Grid";
import { Formik } from "formik";

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
  <Formik
    initialValues={{
      firstName: "",
      lastName: "",
    }}
    onSubmit={(values, { setSubmitting }) => {
      setTimeout(() => setSubmitting(false), 2000);
    }}
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
    values,
      errors,
      touched,
      handleChange,
      handleBlur,
      handleSubmit,
      isSubmitting,
      handleReset,
  }) => (
        <form onSubmit={handleSubmit} onReset={handleReset}>
          <Grid container>

            <Grid item xs={6}>
              <FormControl error={touched.firstName && errors.firstName ? true : false} fullWidth>
                <InputLabel htmlFor="firstName">First name</InputLabel>
                <Input id="firstName" name="firstName" onChange={handleChange} onBlur={handleBlur} value={values.firstName} />
                {touched.firstName && errors.firstName && <FormHelperText>{errors.firstName}</FormHelperText>}
              </FormControl>
            </Grid>

            <Grid item xs={6}>
              <FormControl error={touched.lastName && errors.lastName ? true : false} fullWidth>
                <InputLabel htmlFor="lastName">Last name</InputLabel>
                <Input id="lastName" name="lastName" onChange={handleChange} onBlur={handleBlur} value={values.lastName} />
                {touched.lastName && errors.lastName && <FormHelperText>{errors.lastName}</FormHelperText>}
              </FormControl>
            </Grid>

            <Grid item xs={12} md={6} container>
              <Grid item>
                <FormControl>
                  <Button variant="raised" color="secondary" type="submit" disabled={isSubmitting}>Submit</Button>
                </FormControl>
              </Grid>
              <Grid item>
                <FormControl>
                  <Button variant="raised" type="reset" disabled={isSubmitting}>Reset</Button>
                </FormControl>
              </Grid>
            </Grid>
          </Grid>
        </form>
      )} />
);

export default enhance(Content);