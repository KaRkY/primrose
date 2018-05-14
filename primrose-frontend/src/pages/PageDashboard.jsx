import React from "react";

import NotificationConsumer from "../components/NotificationConsumer";

import compose from "recompose/compose";

import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";

const contentStyle = theme => console.log(theme) || ({

});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = ({ classes, width, style }) => (
  <NotificationConsumer>{
    ({ push }) => (
      <React.Fragment>
        <Typography variant="title">Dela</Typography>
        <Button onClick={() => push({ text: `Dela ${new Date().getTime()}` })}>Dela</Button>
      </React.Fragment>
    )
  }</NotificationConsumer>

);

export default enhance(Content);