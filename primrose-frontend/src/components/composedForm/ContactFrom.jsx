import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Paper from "material-ui/Paper";
import Button from "material-ui/Button";
import IconButton from "material-ui/IconButton";
import ClearIcon from "@material-ui/icons/Clear";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import { InputAdornment } from "material-ui/Input";
import Grid from "material-ui/Grid";
import { Form } from "react-final-form";
import TextField from "../Form/TextField";
import MenuItem from "material-ui/Menu/MenuItem";
import arrayMutators from "final-form-arrays";
import FieldArray from "../Form/FieldArray";


export const style = theme => ({
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

const enhance = compose(
  withStyles(style),
);

const CustomerForm = ({
  classes,
  initialValues,
  onSubmit,
  emailTypes,
  phoneNumberTypes,
}) => (
    <Form
      initialValues={initialValues || {}}
      onSubmit={onSubmit}
      validate={values => true}
      mutators={{
        ...arrayMutators
      }}
      render={({
        handleSubmit,
        submitError,
        reset,
        form: { mutators },
        submitting,
        pristine,
        invalid,
        values,
      }) => (
          <form onSubmit={handleSubmit} onReset={reset}>
            <Grid container spacing={16}>
              {submitError &&
                <Grid item xs={12}>
                  <Paper>
                    <Typography component="pre">Could not save Contact</Typography>
                  </Paper>
                </Grid>
              }

              <Grid item xs={12} md={6}>
                <Paper elevation={2}>
                  <Toolbar>
                    <Typography variant="title">Contact basic data</Typography>
                  </Toolbar>
                  <Grid className={classes.grid} container spacing={16}>
                    <Grid item xs={12}>
                      <TextField name="fullName" label="Full name" fullWidth />
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
                                {Object.keys(emailTypes).map(key => (
                                  <MenuItem key={key} value={key}>{emailTypes[key]}</MenuItem>
                                ))}
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
                    <FieldArray name="phones" label="Phone numbers" push={mutators.push}>
                      {({ fields }) =>
                        fields.map((name, index) => (
                          <Grid container spacing={16} key={name}>
                            <Grid item xs={3} md={2}>
                              <TextField select name={`${name}.type`} label="Type" fullWidth>
                                {Object.keys(phoneNumberTypes).map(key => (
                                  <MenuItem key={key} value={key}>{phoneNumberTypes[key]}</MenuItem>
                                ))}
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

export default enhance(CustomerForm);