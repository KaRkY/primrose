import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({

});


const enhance = compose(
  withStyles(contentStyle)
);

const Content = (props) => (
  <Typography component="pre" variant="body2">{JSON.stringify(props, null, 2)}</Typography>
);

export default enhance(Content);