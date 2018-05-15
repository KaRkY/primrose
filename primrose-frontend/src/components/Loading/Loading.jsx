import React from "react";

import CircularProgress from "@material-ui/core/CircularProgress";

const Loading = ({ classes }) => (
  <div className={classes.root}>
    <CircularProgress className={classes.icon} thiknes="7" size={120} />
  </div>
);

export default Loading;