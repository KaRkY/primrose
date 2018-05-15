import React from "react";

import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";

import NotificationConsumer from "../../components/NotificationConsumer";

const Dashboard = ({ classes, width, style }) => (
  <NotificationConsumer>{
    ({ push }) => (
      <React.Fragment>
        <Typography variant="title">Dela</Typography>
        <Button onClick={() => push({ text: `Dela ${new Date().getTime()}` })}>Dela</Button>
      </React.Fragment>
    )
  }</NotificationConsumer>
);

export default Dashboard;