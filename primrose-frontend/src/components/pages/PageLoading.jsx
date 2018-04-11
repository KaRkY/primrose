import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import Loading from "../Loading";

const contentStyle = theme => ({
  
});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = ({ classes }) => <Loading />;

export default enhance(Content);