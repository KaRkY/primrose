import React from "react";
import * as contacts from "../../api/contacts";
import ContactForm from "../../components/ContactForm";
import NotificationConsumer from "../../components/NotificationConsumer";

const ContactNew = ({
  classes,
  customer,
  customerCode,
  handleView,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
  <NotificationConsumer>{
    ({ push }) => (
      <ContactForm
        onSubmit={values => {
          return contacts.create(values)
            .then(response => {
              handleView(response.data.result);
              return {};
            })
            .catch(error => {
              push({ text: error });
              return {};
            });
        }}
        emailTypes={emailTypes}
        phoneNumberTypes={phoneNumberTypes}
      />
    )
  }</NotificationConsumer>
  );

export default ContactNew;