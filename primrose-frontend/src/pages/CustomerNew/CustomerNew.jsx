import React from "react";
import * as customers from "../../api/customers";
import * as actions from "../../actions";
import meta from "../../store/meta";

import Button from "@material-ui/core/Button";

import Array from "../../components/fields/Array";
import CustomerPersonalInformation from "../../components/forms/CustomerPersonalInformation";
import Email from "../../components/forms/Email";
import Form from "../../components//Form";
import PhoneNumber from "../../components/forms/PhoneNumber";

import NotificationConsumer from "../../components/NotificationConsumer";
import Connect from "../../components/Connect";
import { Compose } from "react-powerplug";

const CustomerNew = ({
  classes,
}) => (
    <Compose components={[
      NotificationConsumer,
      <Connect
        mapStateToProps={
          state => ({
            customerTypes: meta.customerTypes.getData(state),
            customerRelationTypes: meta.customerRelationTypes.getData(state),
            emailTypes: meta.emailTypes.getData(state),
            phoneNumberTypes: meta.phoneNumberTypes.getData(state),
          })
        }

        mapDispatchToProps={{
          handleView: actions.customerViewPage
        }}
      />]
    }>
      {(notify, state) => (
        <Form
          form="customerNew"
          onSubmit={values => {
            return customers.create(values)
              .then(response => {
                state.handleView(response.data.result);
              })
              .catch(error => {
                notify.push({ text: error.message });
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
                types={state.customerTypes}
                relationTypes={state.customerRelationTypes}
              />

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

export default CustomerNew;