import React from "react";
import { withStyles } from "material-ui/styles";
import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import pure from "recompose/pure";
import withWidth, { isWidthDown } from "material-ui/utils/withWidth";
import mapProps from "recompose/mapProps";

import { Curious } from "@curi/react";
import Reboot from "material-ui/Reboot";
import AppDrawer from "./AppDrawer";
import AppToolbar from "./AppToolbar";
import AppContent from "./AppContent";
import AppNav from "./AppNav";
import Typography from "material-ui/Typography";

const styles = theme => ({
  root: {
    position: "relative",
    display: "flex",
    width: "100%",
    height: "100vh",
  },
});

const enhance = compose(
  pure,
  withStyles(styles),
  withWidth(),
  mapProps(({ width, ...rest }) => ({ mobile: isWidthDown("sm", width), ...rest })),
  withStateHandlers(
    ({ mobile }) => ({ drawerOpen: !mobile }),
    {
      onDrawerOpen: ({ updateDrawerOpen }) => () => ({ drawerOpen: true }),
      onDrawerClose: ({ updateDrawerOpen }) => () => ({ drawerOpen: false }),
    }
  ),
);

const App = ({ classes, mobile, onDrawerOpen, onDrawerClose, drawerOpen }) => (
  <React.Fragment>
    <Reboot />
    <div className={classes.root}>
      <AppToolbar mobile={mobile} drawerOpen={drawerOpen} onDrawerOpen={onDrawerOpen}>
        <Typography variant="title" color="inherit">{Response.title}</Typography>
      </AppToolbar>
      <AppDrawer mobile={mobile} open={drawerOpen} onClose={onDrawerClose}>
        <AppNav />
      </AppDrawer>
      <AppContent mobile={mobile} drawerOpen={drawerOpen}>
        <Curious>{({ router, response }) => React.createElement(response.body, { ...response.data, router })}</Curious>
      </AppContent>
    </div>
  </React.Fragment>
);

export default enhance(App);