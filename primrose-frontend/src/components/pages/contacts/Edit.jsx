import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import ContactForm from "../../composedForm/ContactFrom";
import promiseListener from "../../../store/promiseListener";

import { FORM_ERROR } from "final-form";

import * as actions from "../../../actions";
import contacts from "../../../store/contacts";
import meta from "../../../store/meta";

const editContact = promiseListener.createAsyncFunction({
  start: actions.contactEdit.toString(),
  resolve: actions.contactEditFinished.toString(),
  reject: actions.contactEditError.toString(),
});

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  contact: contacts.single.getData(state),
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
  contact,
  goToContact,
  emailTypes,
  phoneNumberTypes,
}) => (
    <ContactForm
      initialValues={contact}
      onSubmit={values => {
        return editContact
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