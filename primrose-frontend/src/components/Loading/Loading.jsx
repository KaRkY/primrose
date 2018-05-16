import React from "react";

import CircularProgress from "@material-ui/core/CircularProgress";

const Loading = ({ classes }) => (
  <div className={classes.root}>
    <div className={classes.icon}>
      <CircularProgress />
    </div>
  </div>
);

export default Loading;