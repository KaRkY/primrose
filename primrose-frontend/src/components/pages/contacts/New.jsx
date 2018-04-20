import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import ContactForm from "../../composedForm/ContactFrom";

import { FORM_ERROR } from "final-form";

import * as actions from "../../../actions";
import meta from "../../../store/meta";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});

const mapDispatchTo = dispatch => ({
  handleSingle: payload => dispatch(actions.contactPage(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  handleSingle,
  emailTypes,
  phoneNumberTypes,
}) => (
    <ContactForm 
      onSubmit={values => {
        return actions.contactCreatePromise(values)
          .then(result => {
            handleSingle(result);
            return {};
          })
          .catch(error => ({ [FORM_ERROR]: error }));
      }}
      emailTypes={emailTypes}
      phoneNumberTypes={phoneNumberTypes}
    />
  );

export default enhance(Content);