import React from "react";
import * as customers from "../../api/customers";
import CustomerForm from "../../components/CustomerForm";
import NotificationConsumer from "../../components/NotificationConsumer";

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
  <NotificationConsumer>{
    ({ push }) => (
      <CustomerForm
        onSubmit={values => {
          return customers.create(values)
            .then(response => {
              handleView(response.data.result);
              return {};
            })
            .catch(error => {
              push({ text: error });
              return {};
            });
        }}
        customerTypes={customerTypes}
        customerRelationTypes={customerRelationTypes}
        emailTypes={emailTypes}
        phoneNumberTypes={phoneNumberTypes}
      />
    )
  }</NotificationConsumer>
  );

export default CustomerNew;