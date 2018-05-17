import React from "react";
import * as contacts from "../../api/contacts";
import ContactForm from "../../components/ContactForm";
import NotificationConsumer from "../../components/NotificationConsumer";

const ContactUpdate = ({
  classes,
  contact,
  handleView,
  emailTypes,
  phoneNumberTypes,
}) => (
  <NotificationConsumer>{
    ({ push }) => (
      <ContactForm
        initialValues={contact}
        onSubmit={values => {
          return contacts.update(values)
            .then(response => {
              handleView(response.data.result);
              return {};
            })
            .catch(error => {
              push({ text: error.message });
              return {};
            });
        }}
        emailTypes={emailTypes}
        phoneNumberTypes={phoneNumberTypes}
      />
    )
  }</NotificationConsumer>
  );

export default ContactUpdate;