import React from "react";
import { connect } from "react-redux";

import { FORM_ERROR } from "final-form";

import * as actions from "../../actions";
import contacts from "../../store/contacts";
import meta from "../../store/meta";

import compose from "recompose/compose";

import ContactForm from "../../components/ContactForm";

import withStyles from "@material-ui/core/styles/withStyles";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  contact: contacts.single.getData(state),
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
  contact,
  handleSingle,
  emailTypes,
  phoneNumberTypes,
}) => (
    <ContactForm
      initialValues={contact}
      onSubmit={values => {
        return actions.contactEditPromise(values)
          .then(result => {
            handleSingle(result);
            return {};
          })
          .catch(error => console.error(error) || ({ [FORM_ERROR]: error }));
      }}
      emailTypes={emailTypes}
      phoneNumberTypes={phoneNumberTypes}
    />
  );

export default enhance(Content);