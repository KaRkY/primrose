import React from "react";

import { FormSection } from "redux-form";

import Button from "@material-ui/core/Button";

import CustomerPersonalInformation from "../../components/forms/CustomerPersonalInformation";
import Array from "../../components/fields/Array";
import Email from "../../components/forms/Email";
import Form from "../../components//Form";
import PhoneNumber from "../../components/forms/PhoneNumber";

const Dashboard = ({ classes, width, style, handleSubmit, handleReset, pristine, submitting }) => (
  <Form
    form="customer"
    onSubmit={console.log}
    initialValues={{
      emails: [
        { type: "home", email: "bla" },
        { type: "work", email: "ble" },
      ]
    }}
  >
    {({ handleSubmit, handleReset, pristine, submitting }) => (
      <form className={classes.root} onSubmit={handleSubmit} onReset={handleReset}>
        <FormSection name="personal">
          <CustomerPersonalInformation
            types={{
              bla: "ble"
            }}

            relationTypes={{
              bla: "ble"
            }}
          />
        </FormSection>

        <div className={classes.horizontal}>
          <Array name="emails" label="Emails">
            <Email
              types={{
                home: "Home",
                work: "Work",
                other: "Other"
              }}
              defaultType="home"
            />
          </Array>
          <Array name="phoneNumbers" label="Phone numbers">
            <PhoneNumber
              types={{
                home: "Home",
                work: "Work",
                other: "Other",
              }}
              defaultType="home"
            />
          </Array>
        </div>

        <div className={classes.actions}>
          <Button disabled={pristine || submitting} variant="raised" type="reset">Reset</Button>
          <Button disabled={pristine || submitting} variant="raised" color="primary" type="submit">Submit</Button>
        </div>
      </form>
    )}
  </Form>
);

export default Dashboard;