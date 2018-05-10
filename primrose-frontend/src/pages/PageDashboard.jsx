import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Typography from "material-ui/Typography";

const contentStyle = theme => console.log(theme) || ({

});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = ({ classes, width, style }) => (
  <Typography variant="title">Dela</Typography>
);

export default enhance(Content);