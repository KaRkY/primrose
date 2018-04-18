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

const createContact = promiseListener.createAsyncFunction({
  start: actions.contactCreate.toString(),
  resolve: actions.contactCreateFinished.toString(),
  reject: actions.contactCreateError.toString(),
});

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  contact: contacts.single.getData(state),
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});

const mapDispatchTo = dispatch => ({
  goToContact: payload => dispatch(actions.contact({ id: payload })),
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
      onSubmit={console.log}
      emailTypes={emailTypes}
      phoneNumberTypes={phoneNumberTypes}
    />
  );

export default enhance(Content);