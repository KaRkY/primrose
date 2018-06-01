import React from "react";
import * as contactView from "../../store/contactView";

import Typography from "@material-ui/core/Typography";

import Connect from "../../components/Connect";

const ContactView = () => (
  <Connect mapStateToProps={state => ({
    contact: contactView.getData(state),
  })}>
    {state => (
      <Typography component="pre" variant="body2">{JSON.stringify(state.contact, null, 2)}</Typography>
    )}
  </Connect>
  );

export default ContactView;