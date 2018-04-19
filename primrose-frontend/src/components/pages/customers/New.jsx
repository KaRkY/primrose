import React from "react";
import compose from "recompose/compose";
import withState from "recompose/withState";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import CustomerForm from "../../composedForm/CustomerForm";
import promiseListener from "../../../store/promiseListener";

import { FORM_ERROR } from "final-form";

import * as actions from "../../../actions";
import meta from "../../../store/meta";

const createCustomer = promiseListener.createAsyncFunction({
  start: actions.customerCreate.toString(),
  resolve: actions.customerCreateFinished.toString(),
  reject: actions.customerCreateError.toString(),
});

const contentStyle = theme => ({

});

// Use state for contacts and accounts.
// Load customer contacts on edit nad save them in state
// 

const mapState = (state, props) => ({
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
  withState("test", "SetTest", ({ customerTypes }) => customerTypes),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  goToCustomer,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
  ...rest
}) => console.log(rest) || (
    <CustomerForm
      onSubmit={values => {
        return createCustomer
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