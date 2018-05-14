import React from "react";

import compose from "recompose/compose";

import CircularProgress from "@material-ui/core/CircularProgress";
import withStyles from "@material-ui/core/styles/withStyles";

const styles = theme => ({
  root: {
    background: theme.palette.action.disabledBackground,
    position: "absolute",
    height: "100%",
    width: "100%",
  },

  icon: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
  },
});

const enhance = compose(
  withStyles(styles)
);

const Loading = ({ classes }) => (
  <div className={classes.root}>
    <CircularProgress className={classes.icon} thiknes="7" size={120} />
  </div>
);

export default enhance(Loading);