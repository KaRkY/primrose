import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import withStateHandlers from "recompose/withStateHandlers";

import Paper from "material-ui/Paper";
import IconButton from "material-ui/IconButton";
import AddIcon from "@material-ui/icons/Add";
import DeleteIcon from "@material-ui/icons/Delete";
import OpenInNewIcon from "@material-ui/icons/OpenInNew";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import Grid from "material-ui/Grid";
import TextField from "../Form/TextField";
import RenderField from "../Form/RenderField";
import arrayMutators from "final-form-arrays";
import { FieldArray } from "react-final-form-arrays";
import Table, {
  TableBody,
  TableCell,
  TableRow,
  TableHead,
} from "material-ui/Table";
import DialogForm from "../Form/DialogForm";
import SimpleForm from "../Form/SimpleForm";
import EmailForm from "../composedForm/EmailForm";
import PhoneForm from "../composedForm/PhoneForm";


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
  withStateHandlers(
    { emailInitialValue: {}, emailDialogOpen: false, phoneInitialValue: {}, phoneDialogOpen: false },
    {
      onOpenEmailForm: (state) => event => ({ ...state, emailDialogOpen: true }),
      onCloseEmailForm: (state) => event => ({ ...state, emailDialogOpen: false }),
      onOpenPhoneForm: (state) => event => ({ ...state, phoneDialogOpen: true }),
      onClosePhoneForm: (state) => event => ({ ...state, phoneDialogOpen: false }),
      setEmailInitialValue: state => value => ({ ...state, emailInitialValue: value }),
      setPhoneInitialValue: state => value => ({ ...state, phoneInitialValue: value }),
    }),
  withStyles(style),
);

const CustomerForm = ({
  classes,
  initialValues,
  emailTypes,
  phoneNumberTypes,
  emailDialogOpen,
  phoneDialogOpen,
  emailInitialValue,
  phoneInitialValue,
  onOpenEmailForm,
  onCloseEmailForm,
  onOpenPhoneForm,
  onClosePhoneForm,
  onSubmit,
  setEmailInitialValue,
  setPhoneInitialValue,
}) => (
    <SimpleForm
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
          <React.Fragment>
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
                    <Paper className={classes.root} elevation={2}>
                      <Toolbar>
                        <Typography variant="title">Emails</Typography>
                        <div className={classes.grow} />
                        <IconButton variant="raised" onClick={onOpenEmailForm}>
                          <AddIcon />
                        </IconButton>
                      </Toolbar>
                      <Table>
                        <TableHead>
                          <TableRow>
                            <TableCell>Type</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell padding="checkbox">Actions</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          <FieldArray name="emails">{({ fields }) => fields.map(((name, index) => (
                            <TableRow key={index}>
                              <TableCell>
                                <RenderField name={`${name}.type`} mapValue={value => emailTypes[value]} />
                              </TableCell>
                              <TableCell>
                                <RenderField name={`${name}.value`} />
                              </TableCell>
                              <TableCell padding="checkbox">
                                <IconButton onClick={() => {
                                  setEmailInitialValue(fields.value[index]);
                                  onOpenEmailForm();
                                }}>
                                  <OpenInNewIcon />
                                </IconButton>
                                <IconButton onClick={() => mutators.remove("phones", index)}>
                                  <DeleteIcon />
                                </IconButton>
                              </TableCell>
                            </TableRow>
                          )))}</FieldArray>
                        </TableBody>
                      </Table>
                    </Paper>
                  </Grid>

                  <Grid item>
                    <Paper className={classes.root} elevation={2}>
                      <Toolbar>
                        <Typography variant="title">Phones</Typography>
                        <div className={classes.grow} />
                        <IconButton variant="raised" onClick={onOpenPhoneForm}>
                          <AddIcon />
                        </IconButton>
                      </Toolbar>
                      <Table>
                        <TableHead>
                          <TableRow>
                            <TableCell>Type</TableCell>
                            <TableCell>Phone</TableCell>
                            <TableCell padding="checkbox">Actions</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          <FieldArray name="phones">{({ fields }) => fields.map(((name, index) => (
                            <TableRow key={index}>
                              <TableCell>
                                <RenderField name={`${name}.type`} mapValue={value => phoneNumberTypes[value]} />
                              </TableCell>
                              <TableCell>
                                <RenderField name={`${name}.value`} />
                              </TableCell>
                              <TableCell padding="checkbox">
                                <IconButton onClick={() => {
                                  setPhoneInitialValue(fields.value[index]);
                                  onOpenEmailForm();
                                }}>
                                  <OpenInNewIcon />
                                </IconButton>
                                <IconButton onClick={() => mutators.remove("phones", index)}>
                                  <DeleteIcon />
                                </IconButton>
                              </TableCell>
                            </TableRow>
                          )))}</FieldArray>
                        </TableBody>
                      </Table>
                    </Paper>
                  </Grid>
                </Grid>
              </Grid>
            </Grid>

            <DialogForm
              title="Add Email"
              onSubmit={(values, { close }) => {
                mutators.push("emails", values);
                close();
              }}
              open={emailDialogOpen}
              onOpen={onOpenEmailForm}
              onClose={onCloseEmailForm}
              initialValues={emailInitialValue.type ? emailInitialValue : {
                type: emailTypes["home"] ? "home" : undefined
              }}
              render={props =>
                <EmailForm
                  types={emailTypes}
                  {...props}
                />}
            />
            <DialogForm
              title="Add Phone"
              onSubmit={(values, { close }) => {
                mutators.push("phones", values);
                close();
              }}
              open={phoneDialogOpen}
              onOpen={onOpenPhoneForm}
              onClose={onClosePhoneForm}
              initialValues={phoneInitialValue.type ? phoneInitialValue : {
                type: phoneNumberTypes["home"] ? "home" : undefined
              }}
            >{props =>
              <PhoneForm
                types={phoneNumberTypes}
                {...props}
              />}</DialogForm>
          </React.Fragment>
        )} />
  );

export default enhance(CustomerForm);