import React from "react";
import * as contacts from "../../api/contacts";
import * as actions from "../../actions";
import meta from "../../store/meta";

import Button from "@material-ui/core/Button";

import Array from "../../components/fields/Array";
import ContactPersonalInformation from "../../components/forms/ContactPersonalInformation";
import Email from "../../components/forms/Email";
import Form from "../../components//Form";
import PhoneNumber from "../../components/forms/PhoneNumber";

import NotificationConsumer from "../../components/NotificationConsumer";
import Connect from "../../components/Connect";
import { Compose } from "react-powerplug";

const ContactNew = ({
  classes,
  handleView,
  emailTypes,
  phoneNumberTypes,
}) => (
  <Compose components={[
    NotificationConsumer,
    <Connect
      mapStateToProps={
        state => ({
          emailTypes: meta.emailTypes.getData(state),
          phoneNumberTypes: meta.phoneNumberTypes.getData(state),
        })
      }

      mapDispatchToProps={{
        handleView: actions.contactViewPage
      }}
    />]
  }>
    {(notify, state) => (
        <Form
          form="contactNew"
          onSubmit={values => {
            return contacts.create(values)
              .then(response => {
                state.handleView(response.data.result);
              })
              .catch(error => {
                state.push({ text: error.message });
              });
          }}
        >
          {({ handleSubmit, handleReset, pristine, submitting }) => (
            <form className={classes.root} onSubmit={handleSubmit} onReset={handleReset}>
              <ContactPersonalInformation />

              <div className={classes.horizontal}>
                <Array name="emails" label="Emails" initialValues={{ type: "home" }}>
                  <Email
                    types={state.emailTypes}
                  />
                </Array>
                <Array name="phoneNumbers" label="Phone numbers" initialValues={{ type: "home" }}>
                  <PhoneNumber
                    types={state.phoneNumberTypes}
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
    </Compose>
  );

export default ContactNew;