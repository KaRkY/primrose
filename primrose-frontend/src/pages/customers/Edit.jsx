import React from "react";
import { connect } from "react-redux";

import { FORM_ERROR } from "final-form";

import * as actions from "../../actions";
import * as location from "../../store/location";
import customers from "../../store/customers";
import meta from "../../store/meta";

import compose from "recompose/compose";

import CustomerForm from "../../components/CustomerForm";

import withStyles from "@material-ui/core/styles/withStyles";

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
  handleSingle: payload => dispatch(actions.customerPage(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  customer,
  customerId,
  handleSingle,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
    <CustomerForm
      initialValues={customer}
      onSubmit={values => {
        return actions.customerEditPromise(values)
          .then(result => {
            handleSingle(result);
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