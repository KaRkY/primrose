import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import contacts from "../../../store/contacts";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  contact: contacts.single.getData(state),
});

const mapDispatchTo = dispatch => ({

});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  contact,
}) => (
  <Typography component="pre" variant="body2">{JSON.stringify(contact, null, 2)}</Typography>
);

export default enhance(Content);