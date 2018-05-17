import React from "react";
import * as customers from "../../api/customers";
import CustomerForm from "../../components/CustomerForm";
import NotificationConsumer from "../../components/NotificationConsumer";

const CustomerUpdate = ({
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
        initialValues={customer}
        onSubmit={values => {
          return customers.update(values)
            .then(response => {
              handleView(response.data.result);
              return {};
            })
            .catch(error => {
              push({ text: error.message });
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

export default CustomerUpdate;