import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({
  
});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = ({ classes }) => <Typography variant="body2">Not Found</Typography>;

export default enhance(Content);