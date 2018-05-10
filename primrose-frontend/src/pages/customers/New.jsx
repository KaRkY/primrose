import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import CustomerForm from "../../components/composedForm/CustomerForm";

import { FORM_ERROR } from "final-form";

import * as actions from "../../actions";
import meta from "../../store/meta";

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
  handleSingle: payload => dispatch(actions.customerPage(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  handleSingle,
  customerTypes,
  customerRelationTypes,
  emailTypes,
  phoneNumberTypes,
}) => (
    <CustomerForm
      onSubmit={values => {
        return actions.customerCreatePromise(values)
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