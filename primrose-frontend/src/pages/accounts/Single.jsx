import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import accounts from "../../store/accounts";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  account: accounts.single.getData(state),
});

const mapDispatchTo = dispatch => ({

});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle),
);

const Content = ({
  account,
}) => (
  <Typography component="pre" variant="body2">{JSON.stringify(account, null, 2)}</Typography>
);

export default enhance(Content);