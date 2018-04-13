import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import * as actions from "../../actions";

import Paper from "material-ui/Paper";
import Button from "material-ui/Button";
import IconButton from "material-ui/IconButton";
import ClearIcon from "@material-ui/icons/Clear";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import { InputAdornment } from "material-ui/Input";
import Grid from "material-ui/Grid";
import { Form } from "react-final-form";
import { FORM_ERROR } from "final-form";
import TextField from "../Form/TextField";
import MenuItem from "material-ui/Menu/MenuItem";
import arrayMutators from "final-form-arrays";
import FieldArray from "../Form/FieldArray";
import promiseListener from "../../store/promiseListener";

const createCustomer = promiseListener.createAsyncFunction({
  start: actions.customerCreate.toString(),
  resolve: actions.customerCreateFinished.toString(),
  reject: actions.customerCreateError.toString(),
});

const contentStyle = theme => ({
  root: theme.mixins.gutters({
  }),

  buttons: {
    paddingTop: 3 * theme.spacing.unit,
  },

  grid: {
    padding: 3 * theme.spacing.unit,
  },

  grow: {
    flex: "1 1 auto",
  },
});



const mapState = (state, props) => ({

});

const mapDispatchTo = dispatch => ({
  goToCustomer: payload => dispatch(actions.customer(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  goToCustomer,
}) => (
    <Form
      onSubmit={values => {
        return createCustomer
          .asyncFunction(values)
          .then(result => {
            goToCustomer(result);
            return {};
          })
          .catch(error => ({ [FORM_ERROR]: error }));
      }}
      validate={values => true}
      mutators={{
        ...arrayMutators
      }}
      render={({
        handleSubmit,
        submitError,
        reset,
        mutators,
        submitting,
        pristine,
        invalid,
        values
      }) => (
          <form onSubmit={handleSubmit} onReset={reset}>
            <Grid container spacing={16}>
              {submitError &&
                <Grid item xs={12}>
                  <Paper>
                    <Typography component="pre">{JSON.stringify(submitError, null, 2)}</Typography>
                  </Paper>
                </Grid>
              }

              <Grid item xs={12} md={6}>
                <Paper elevation={2}>
                  <Toolbar>
                    <Typography variant="title">Customer basic data</Typography>
                  </Toolbar>
                  <Grid className={classes.grid} container spacing={16}>
                    <Grid item xs={12}>
                      <TextField name="fullName" label="Full name" fullWidth />
                    </Grid>

                    <Grid item xs={12}>
                      <TextField name="displayName" label="Display name" fullWidth />
                    </Grid>

                    <Grid item xs={12} md={6}>
                      <TextField select name="customerType" label="Customer type" fullWidth>
                        <MenuItem value="person">Person</MenuItem>
                        <MenuItem value="company">Company</MenuItem>
                      </TextField>
                    </Grid>

                    <Grid item xs={12} md={6}>
                      <TextField select name="customerRelationType" label="Customer relation type" fullWidth>
                        <MenuItem value="customer">Customer</MenuItem>
                        <MenuItem value="partner">Partner</MenuItem>
                        <MenuItem value="investor">Investor</MenuItem>
                        <MenuItem value="reseller">Reseller</MenuItem>
                      </TextField>
                    </Grid>

                    <Grid item xs={12}>
                      <TextField
                        multiline
                        rows={5}
                        rowsMax={5}
                        name="description"
                        label="Description"
                        fullWidth />
                    </Grid>
                  </Grid>
                </Paper>
              </Grid>

              <Grid item xs={12} md={6}>
                <Grid container spacing={16} direction="column" alignItems="stretch">
                  <Grid item>
                    <FieldArray name="emails" label="Emails" push={mutators.push}>
                      {({ fields }) =>
                        fields.map((name, index) => (
                          <Grid container spacing={16} key={name}>
                            <Grid item xs={3}>
                              <TextField select name={`${name}.type`} label="Type" fullWidth>
                                <MenuItem value="work">Work</MenuItem>
                                <MenuItem value="home">Home</MenuItem>
                                <MenuItem value="other">Other</MenuItem>
                              </TextField>
                            </Grid>
                            <Grid item xs={9}>
                              <TextField name={`${name}.value`} label="Email" fullWidth
                                InputProps={{
                                  endAdornment: (
                                    <InputAdornment position="end">
                                      <IconButton onClick={() => mutators.remove("emails", index)}>
                                        <ClearIcon />
                                      </IconButton>
                                    </InputAdornment>
                                  )
                                }} />
                            </Grid>
                          </Grid>
                        ))}
                    </FieldArray>
                  </Grid>

                  <Grid item>
                    <FieldArray name="phoneNumbers" label="Phone numbers" push={mutators.push}>
                      {({ fields }) =>
                        fields.map((name, index) => (
                          <Grid container spacing={16} key={name}>
                            <Grid item xs={3} md={2}>
                              <TextField select name={`${name}.type`} label="Type" fullWidth>
                                <MenuItem value="work">Work</MenuItem>
                                <MenuItem value="home">Home</MenuItem>
                                <MenuItem value="other">Other</MenuItem>
                              </TextField>
                            </Grid>
                            <Grid item xs={9} md={10}>
                              <TextField name={`${name}.value`} label="Phone" fullWidth
                                InputProps={{
                                  endAdornment: (
                                    <InputAdornment position="end">
                                      <IconButton onClick={() => mutators.remove("phoneNumbers", index)}>
                                        <ClearIcon />
                                      </IconButton>
                                    </InputAdornment>
                                  )
                                }} />
                            </Grid>
                          </Grid>
                        ))}
                    </FieldArray>
                  </Grid>
                </Grid>
              </Grid>

              <Grid item container spacing={16}>
                <Grid item>
                  <Button variant="raised" color="secondary" type="submit" disabled={submitting || pristine}>Submit</Button>
                </Grid>
                <Grid item>
                  <Button variant="raised" type="reset" disabled={submitting}>Reset</Button>
                </Grid>
              </Grid>
            </Grid>
          </form>
        )} />
  );

export default enhance(Content);