import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Typography from "material-ui/Typography";
import Button from "material-ui/Button";
import NotificationConsumer from "../components/App/NotificationConsumer";

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