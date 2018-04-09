import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Paper from "material-ui/Paper";

const contentStyle = theme => ({
  root: theme.mixins.gutters({
  }),
});


const enhance = compose(
  withStyles(contentStyle)
);

/*
  Display loading indicator
 */
const Content = ({
  classes,
}) => (
    <Paper>Edit</Paper>
  );

export default enhance(Content);