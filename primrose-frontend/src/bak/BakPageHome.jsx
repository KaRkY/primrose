import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import withWidth, { isWidthDown } from "material-ui/utils/withWidth";
import yup from "yup";
import validate from "../../validate";

import Paper from "material-ui/Paper";
import Button from "material-ui/Button";
import Grid from "material-ui/Grid";
import { Form } from "react-final-form";
import MonacoField from "../Form/MonacoField";

const contentStyle = theme => ({
  container: {
    padding: 2 * theme.spacing.unit,
  },

  buttons: {
    paddingTop: 2 * theme.spacing.unit,
  },
});

const enhance = compose(
  withWidth(),
  withStyles(contentStyle)
);

var schema = yup.object().shape({
  code: yup.string().required().nullable(),
});

const Content = ({ classes, width }) => (
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

            <MonacoField name="code" height={
              isWidthDown("md", width) ? 
              400 : 
              isWidthDown("lg", width) ? 
              600 :
              800} options={{
              automaticLayout: true
            }} />

            <Grid className={classes.buttons} spacing={16} container>
              <Grid item>
                <Button variant="raised" color="secondary" type="submit" disabled={submitting || pristine || invalid}>Submit</Button>
              </Grid>
              <Grid item>
                <Button variant="raised" type="reset" disabled={submitting}>Reset</Button>
              </Grid>
            </Grid>
          </form>
        )} />
  </Paper>
);

export default enhance(Content);