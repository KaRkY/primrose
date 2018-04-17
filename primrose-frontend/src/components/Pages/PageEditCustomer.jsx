import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import * as customer from "../../store/customer";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  customer: customer.getData(state),
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