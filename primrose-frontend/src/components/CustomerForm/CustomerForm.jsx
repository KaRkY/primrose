import React from "react";
import arrayMutators from "final-form-arrays";
import { FieldArray } from "react-final-form-arrays";

import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";

import Grid from "@material-ui/core/Grid";
import IconButton from "@material-ui/core/IconButton";
import MenuItem from "@material-ui/core/MenuItem";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";

import AddIcon from "@material-ui/icons/Add";
import DeleteIcon from "@material-ui/icons/Delete";
import OpenInNewIcon from "@material-ui/icons/OpenInNew";
import PersonAddIcon from "@material-ui/icons/PersonAdd";
import SearchIcon from "@material-ui/icons/Search";

import DialogForm from "../DialogForm";
import EmailForm from "../EmailForm";
import PhoneForm from "../PhoneForm";
import RenderField from "../RenderField";
import SimpleForm from "../SimpleForm";
import TextField from "../TextField";


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
  customerTypes,
  customerRelationTypes,
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
      initialValues={initialValues || {
        type: customerTypes["person"] ? "person" : undefined,
        relationType: customerRelationTypes["customer"] ? "customer" : undefined,
      }}
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
                    <Typography component="pre">Could not save Customer</Typography>
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
                      <TextField select name="type" label="Customer type" fullWidth>
                        {Object.keys(customerTypes).map(key => (
                          <MenuItem key={key} value={key}>{customerTypes[key]}</MenuItem>
                        ))}
                      </TextField>
                    </Grid>

                    <Grid item xs={12} md={6}>
                      <TextField select name="relationType" label="Customer relation type" fullWidth>
                        {Object.keys(customerRelationTypes).map(key => (
                          <MenuItem key={key} value={key}>{customerRelationTypes[key]}</MenuItem>
                        ))}
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
                    <Paper elevation={2}>
                      <Toolbar>
                        <Typography variant="title">Emails</Typography>
                        <div className={classes.grow} />
                        <IconButton variant="raised" onClick={() => {
                          setEmailInitialValue({});
                          onOpenEmailForm();
                        }}>
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
                            <TableRow key={name}>
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
                                <IconButton onClick={() => mutators.remove("emails", index)}>
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
                    <Paper elevation={2}>
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

              <Grid item xs={12}>
                <Paper>
                  <Toolbar>
                    <Typography variant="title">Contacts</Typography>
                    <div className={classes.grow} />
                    <Tooltip
                      title={"New contact"}
                      enterDelay={300}
                    >
                      <IconButton>
                        <PersonAddIcon />
                      </IconButton>
                    </Tooltip>
                    <Tooltip
                      title={"Find Contact"}
                      enterDelay={300}
                    >
                      <IconButton>
                        <SearchIcon />
                      </IconButton>
                    </Tooltip>
                  </Toolbar>
                </Paper>
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
              render={props =>
                <PhoneForm
                  types={phoneNumberTypes}
                  {...props}
                />}
            />
          </React.Fragment>
        )} />
  );

export default enhance(CustomerForm);