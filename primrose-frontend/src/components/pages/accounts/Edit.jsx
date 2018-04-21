import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import CustomerForm from "../../composedForm/CustomerForm";
import promiseListener from "../../../store/promiseListener";

import { FORM_ERROR } from "final-form";

import * as actions from "../../../actions";
import customers from "../../../store/customers";
import meta from "../../../store/meta";

const createCustomer = promiseListener.createAsyncFunction({
  start: actions.customerCreate.toString(),
  resolve: actions.customerCreateFinished.toString(),
  reject: actions.customerCreateError.toString(),
});

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  customer: customers.single.getData(state),
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});


const mapDispatchTo = dispatch => ({
  handleSingle: payload => dispatch(actions.customerPage(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  customer,
  handleSingle,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
    <CustomerForm
      initialValues={customer}
      onSubmit={console.log}
      customerTypes={customerTypes}
      customerRelationTypes={customerRelationTypes}
      emailTypes={emailTypes}
      phoneNumberTypes={phoneNumberTypes}
    />
  );

export default enhance(Content);