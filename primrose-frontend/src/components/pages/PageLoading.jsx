import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import Loading from "../Loading";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({
  
});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = ({ classes }) => <Loading />;

export default enhance(Content);