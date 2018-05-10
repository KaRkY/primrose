import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import CustomerForm from "../../components/composedForm/CustomerForm";
import promiseListener from "../../store/promiseListener";

import { FORM_ERROR } from "final-form";

import * as actions from "../../actions";
import meta from "../../store/meta";

const createCustomer = promiseListener.createAsyncFunction({
  start: actions.customerCreate.toString(),
  resolve: actions.customerCreateFinished.toString(),
  reject: actions.customerCreateError.toString(),
});

const contentStyle = theme => ({

});



const mapState = (state, props) => ({
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});

const mapDispatchTo = dispatch => ({
  goToCustomer: payload => dispatch(actions.customerPage({ id: payload })),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  goToCustomer,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
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