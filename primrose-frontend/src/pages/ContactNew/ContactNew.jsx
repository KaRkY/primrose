import React from "react";
import * as contacts from "../../api/contacts";

import Button from "@material-ui/core/Button";

import Array from "../../components/fields/Array";
import ContactPersonalInformation from "../../components/forms/ContactPersonalInformation";
import Email from "../../components/forms/Email";
import Form from "../../components//Form";
import NotificationConsumer from "../../components/NotificationConsumer";
import PhoneNumber from "../../components/forms/PhoneNumber";

const ContactNew = ({
  classes,
  handleView,
  emailTypes,
  phoneNumberTypes,
}) => (
  <NotificationConsumer>
      {({ push }) => (
        <Form
          form="contactNew"
          onSubmit={values => {
            return contacts.create(values)
              .then(response => {
                handleView(response.data.result);
              })
              .catch(error => {
                push({ text: error.message });
              });
          }}
        >
          {({ handleSubmit, handleReset, pristine, submitting }) => (
            <form className={classes.root} onSubmit={handleSubmit} onReset={handleReset}>
              <ContactPersonalInformation />

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

export default ContactNew;