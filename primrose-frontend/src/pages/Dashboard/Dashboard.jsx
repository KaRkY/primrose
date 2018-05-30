import React from "react";

import { Form, NestedField } from "react-form";

import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";

import CustomerPersonalInformation from "../../components/forms/CustomerPersonalInformation";
import Array from "../../components/fields/Array";
import Email from "../../components/forms/Email";
import PhoneNumber from "../../components/forms/PhoneNumber";

const Dashboard = ({ classes, width, style, toggleLoading, loading }) => (
  <Form 
    defaultValues={{
      emails: [
        {type: "home", email: "asdfsdf"},
        {type: "other", email: "123edaw"},
      ]
    }}
  onSubmit={values => console.log(values)}>
    {formApi => (
      <form className={classes.root} onSubmit={formApi.submitForm} onReset={formApi.resetAll}>
        <CustomerPersonalInformation
          types={{
            bla: "ble"
          }}

          relationTypes={{
            bla: "ble"
          }}
        />

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