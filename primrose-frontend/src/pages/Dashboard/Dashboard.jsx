import React from "react";

import { Form, NestedField } from "react-form";

import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";

import CustomerPersonalInformation from "../../components/forms/CustomerPersonalInformation";
import Array from "../../components/fields/Array";
import Email from "../../components/forms/Email";
import PhoneNumber from "../../components/forms/PhoneNumber";

import * as yup from "yup";

var schema = yup.object().shape({
  fullName: yup.string().required(),
  displayName: yup.string(),
  type: yup.string().required(),
  relationType: yup.string().required(),
  emails: yup.array().min(1).of(yup.object().shape({
    type: yup.string().required(),
    email: yup.string().email().required(),
  })),
})

const Dashboard = ({ classes, width, style, toggleLoading, loading }) => (
  <Form dontValidateOnMount 
    defaultValues={{
      emails: [
        { type: "home", email: "asdfsdf" },
        { type: "other", email: "123edaw" },
      ]
    }}
    validate={values => {
      try {
        schema.validateSync(values, { abortEarly: false });
        return { };
      } catch (errors) {
        const err = errors.inner.reduce((acc, error) => {
          acc[error.path] = error.message;
          return acc;
        }, {});
        console.log(errors);
        return err;
      }
    }}
    onSubmit={values => console.log(values)}>
    {formApi => (
      <form className={classes.root} onSubmit={formApi.submitForm} onReset={formApi.resetAll}>
        <Paper>
          <Toolbar>
            <Typography variant="title">Personal information</Typography>
          </Toolbar>
          <CustomerPersonalInformation
            className={classes.panel}

            types={{
              bla: "ble"
            }}

            relationTypes={{
              bla: "ble"
            }}
          />
        </Paper>

        <div className={classes.horizontal}>
          <Array field="emails" label="Emails">
            <Email
              types={{
                home: "Home",
                work: "Work",
                other: "Other"
              }}
              defaultType="home"
            />
          </Array>
          <Array field="phoneNumbers" label="Phone numbers">
            <PhoneNumber
              types={{
                home: "Home",
                work: "Work",
                other: "Other"
              }}
              defaultType="home"
            />
          </Array>
        </div>

        <div className={classes.actions}>
          <Button variant="raised" type="reset">Reset</Button>
          <Button variant="raised" color="primary" type="submit">Submit</Button>
        </div>
      </form>
    )}
  </Form>
);

export default Dashboard;