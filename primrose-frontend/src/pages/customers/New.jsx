import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import CustomerForm from "../../components/composedForm/CustomerForm";
import NotificationConsumer from "../../components/App/NotificationConsumer";

import { FORM_ERROR } from "final-form";

import * as actions from "../../actions";
import * as customers from "../../api/customers";
import meta from "../../store/meta";

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
    <NotificationConsumer>{
      ({ push }) => (
        <CustomerForm
          onSubmit={values => {
            return customers.create(values)
              .then(result => {
                goToCustomer(result);
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

export default enhance(Content);