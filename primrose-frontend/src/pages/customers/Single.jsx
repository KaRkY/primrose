import React from "react";
import { connect } from "react-redux";
import customers from "../../store/customers";

import compose from "recompose/compose";

import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  customer: customers.single.getData(state),
});

const mapDispatchTo = dispatch => ({

});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  customer,
}) => (
  <Typography component="pre" variant="body2">{JSON.stringify(customer, null, 2)}</Typography>
);

export default enhance(Content);