import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import { CircularProgress } from "material-ui/Progress";

const styles = theme => ({
  root: {
    background: theme.palette.action.disabledBackground,
  },

  icon: {
    color: theme.palette.action.disabledBackground
  }
});

const enhance = compose(
  withStyles(styles)
);

const Loading = ({ classes }) => (
  <div className={classes.root}>
    <CircularProgress className={classes.icon} />
  </div>
);

export default enhance(Loading);

const isEmptyChildren = children => React.Children.count(children) === 0;
