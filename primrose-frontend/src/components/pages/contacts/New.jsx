import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import ContactForm from "../../composedForm/ContactFrom";
import promiseListener from "../../../store/promiseListener";

import { FORM_ERROR } from "final-form";

import * as actions from "../../../actions";
import meta from "../../../store/meta";

const createContact = promiseListener.createAsyncFunction({
  start: actions.contactCreate.toString(),
  resolve: actions.contactCreateFinished.toString(),
  reject: actions.contactCreateError.toString(),
});

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});

const mapDispatchTo = dispatch => ({
  goToContact: payload => dispatch(actions.contactPage({ contact: payload })),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  classes,
  goToContact,
  emailTypes,
  phoneNumberTypes,
}) => (
    <ContactForm 
      onSubmit={values => {
        return createContact
          .asyncFunction(values)
          .then(result => {
            goToContact(result);
            return {};
          })
          .catch(error => console.error(error) || ({ [FORM_ERROR]: error }));
      }}
      emailTypes={emailTypes}
      phoneNumberTypes={phoneNumberTypes}
    />
  );

export default enhance(Content);