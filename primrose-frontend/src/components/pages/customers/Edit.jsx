import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import CustomerForm from "../../composedForm/CustomerForm";
import promiseListener from "../../../store/promiseListener";

import { FORM_ERROR } from "final-form";

import * as actions from "../../../actions";
import * as location from "../../../store/location";
import customers from "../../../store/customers";
import meta from "../../../store/meta";

const editCustomer = promiseListener.createAsyncFunction({
  start: actions.customerEdit.toString(),
  resolve: actions.customerEditFinished.toString(),
  reject: actions.customerEditError.toString(),
});

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  customer: customers.single.getData(state),
  customerId: location.getCurrentData(state).customer,
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});


const mapDispatchTo = dispatch => ({
  goToCustomer: payload => dispatch(actions.customerPage({ customer: payload })),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  customer,
  customerId,
  goToCustomer,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
    <CustomerForm
      initialValues={customer}
      onSubmit={values => {
        return editCustomer
          .asyncFunction(values)
          .then(result => {
            goToCustomer(result);
            return {};
          })
          .catch(error => console.error(error) || ({ [FORM_ERROR]: error }));
      }}
      customerTypes={customerTypes}
      customerRelationTypes={customerRelationTypes}
      emailTypes={emailTypes}
      phoneNumberTypes={phoneNumberTypes}
    />
  );

export default enhance(Content);