import React from "react";
import * as customers from "../../api/customers";

import Button from "@material-ui/core/Button";

import Array from "../../components/fields/Array";
import CustomerPersonalInformation from "../../components/forms/CustomerPersonalInformation";
import Email from "../../components/forms/Email";
import Form from "../../components//Form";
import NotificationConsumer from "../../components/NotificationConsumer";
import PhoneNumber from "../../components/forms/PhoneNumber";

const CustomerNew = ({
  classes,
  customer,
  customerCode,
  handleView,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
    <NotificationConsumer>
      {({ push }) => (
        <Form
          form="customerNew"
          onSubmit={values => {
            return customers.create(values)
              .then(response => {
                handleView(response.data.result);
              })
              .catch(error => {
                push({ text: error.message });
              });
          }}
          initialValues={{
            type: "person",
            relationType: "customer",
          }}
        >
          {({ handleSubmit, handleReset, pristine, submitting }) => (
            <form className={classes.root} onSubmit={handleSubmit} onReset={handleReset}>
              <CustomerPersonalInformation
                types={customerTypes}
                relationTypes={customerRelationTypes}
              />

              <div className={classes.horizontal}>
                <Array name="emails" label="Emails" initialValues={{ type: "home" }}>
                  <Email
                    types={emailTypes}
                  />
                </Array>
                <Array name="phoneNumbers" label="Phone numbers" initialValues={{ type: "home" }}>
                  <PhoneNumber
                    types={phoneNumberTypes}
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
      )}
    </NotificationConsumer>
  );

export default CustomerNew;