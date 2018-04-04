import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import yup from "yup";
import validate from "../../validate";

import Paper from "material-ui/Paper";
import Input, { InputLabel } from "material-ui/Input";
import Checkbox from "material-ui/Checkbox";
import Button from "material-ui/Button";
import { FormControl, FormHelperText, FormGroup, FormControlLabel, FormLabel } from "material-ui/Form";
import { RadioGroup } from "material-ui/Radio";
import Grid from "material-ui/Grid";
import { Form, Field } from "react-final-form";
import TextField from "../Form/TextField";
import DateTimeField from "../Form/DateTimeField";
import CheckboxField from "../Form/CheckboxField";
import RadioField from "../Form/RadioField";
import SwitchField from "../Form/SwitchField";

const contentStyle = theme => ({
  container: {
    padding: theme.spacing.unit,
  },
});

const enhance = compose(
  withStyles(contentStyle)
);

var schema = yup.object().shape({
  firstName: yup.string().required().nullable(),
  lastName: yup.string().required().nullable(),
  validFrom: yup.date().required().nullable(),
  validTo: yup.date().required().nullable(),
});

const Content = ({ classes }) => (
  <Paper classes={{ root: classes.container }}>
    <Form
      initialValues={{
        firstName: null,
        lastName: null,
        admin: null,
        bla: null,
        group: null,
        description: null,
        validFrom: null,
        validTo: null,
      }}
      onSubmit={({ validFrom, validTo, ...rest }) => console.log({
        ...rest,
        validFrom: validFrom && validFrom.format(),
        validTo: validTo && validTo.format(),
      })}
      validate={values => validate(values, schema)/*Slow check*/}
      render={({
        handleSubmit,
        reset,
        submitting,
        pristine,
        invalid,
        values
      }) => (
          <form onSubmit={handleSubmit} onReset={reset}>
            <Grid container>

              <Grid item xs={12} md={6}>
                <TextField name="firstName" label="First name" fullWidth />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField name="lastName" label="Last name" fullWidth />
              </Grid>

              <Grid item xs={12} md={6}>
                <DateTimeField name="validFrom" label="Valid from" fullWidth />
              </Grid>

              <Grid item xs={12} md={6}>
                <DateTimeField name="validTo" label="Valid to" fullWidth />
              </Grid>

              <Grid item xs={12}>
                <FormControl component="fieldset">
                  <FormLabel component="legend">Is admin</FormLabel>
                  <FormGroup row>
                    <CheckboxField name="admin" label="Admin" />
                  </FormGroup>
                </FormControl>
              </Grid>

              <Grid item xs={12}>
                <FormControl component="fieldset">
                  <FormLabel component="legend">Is admin</FormLabel>
                  <FormGroup row>
                    <SwitchField name="bla" label="Admin" />
                  </FormGroup>
                </FormControl>
              </Grid>

              <Grid item xs={12}>
                <FormControl component="fieldset">
                  <FormLabel component="legend">Select user group</FormLabel>
                  <RadioGroup row>
                    <RadioField name="group" label="Admin" value="admin" />
                    <RadioField name="group" label="User" value="user" />
                  </RadioGroup>
                </FormControl>
              </Grid>

              <Grid item xs={12}>
                <TextField name="description" label="Description" multiline fullWidth rows={4} rowsMax={4} />
              </Grid>

              <Grid item xs={12} md={6} container>
                <Grid item>
                  <FormControl>
                    <Button variant="raised" color="secondary" type="submit" disabled={submitting || pristine || invalid}>Submit</Button>
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
  </Paper>
);

export default enhance(Content);