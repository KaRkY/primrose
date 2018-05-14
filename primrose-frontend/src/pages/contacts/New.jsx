import React from "react";
import { connect } from "react-redux";

import { FORM_ERROR } from "final-form";

import * as actions from "../../actions";
import meta from "../../store/meta";

import compose from "recompose/compose";

import ContactForm from "../../components/ContactForm";

import withStyles from "@material-ui/core/styles/withStyles";

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
  emailTypes,
  phoneNumberTypes,
  handleSingle,
}) => (
    <React.Fragment>
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
    </React.Fragment>
  );

export default enhance(Content);