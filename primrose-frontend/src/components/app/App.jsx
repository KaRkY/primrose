import React from "react";
import { withStyles } from "material-ui/styles";
import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import pure from "recompose/pure";
import withWidth, { isWidthDown } from "material-ui/utils/withWidth";
import mapProps from "recompose/mapProps";

import Reboot from "material-ui/Reboot";
import AppDrawer from "./AppDrawer";
import AppToolbar from "./AppToolbar";
import AppContent from "./AppContent";
import AppNav from "./AppNav";
import Typography from "material-ui/Typography";

const styles = theme => ({
  "@global": {
    "#nprogress": {
      pointerEvents: "none",
      "& .bar": {
        position: "fixed",
        background: theme.palette.type === "light" ? theme.palette.common.black : theme.palette.common.white,
        borderRadius: 1,
        zIndex: theme.zIndex.tooltip,
        top: 0,
        left: 0,
        width: "100%",
        height: 2,
      },
      "& dd, & dt": {
        position: "absolute",
        top: 0,
        height: 2,
        boxShadow: `${
          theme.palette.type === "light" ? theme.palette.common.black : theme.palette.common.white
        } 1px 0 6px 1px`,
        borderRadius: "100%",
        animation: "nprogress-pulse 2s ease-out 0s infinite",
      },
      "& dd": {
        opacity: 0.6,
        width: 20,
        right: 0,
        clip: "rect(-6px,22px,14px,10px)",
      },
      "& dt": {
        opacity: 0.6,
        width: 180,
        right: -80,
        clip: "rect(-6px,90px,14px,-6px)",
      },
    },
    "@keyframes nprogress-pulse": {
      "30%": {
        opacity: 0.6,
      },
      "60%": {
        opacity: 0,
      },
      to: {
        opacity: 0.6,
      },
    },
  },
  appFrame: {
    position: "relative",
    display: "flex",
    width: "100%",
    height: "100vh",
  },

  activeMenuItem: {
    backgroundColor: theme.palette.primary.main,
    "& $activeMenuItemText, & $activeMenuItemIcon": {
      color: theme.palette.primary.contrastText,
    },
    "&:hover": {
      backgroundColor: theme.palette.primary.dark,
    },
  },
  activeMenuItemText: {},
  activeMenuItemIcon: {},
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

const App = ({ classes, mobile, router, response: { body: Body, ...rest}, onDrawerOpen, onDrawerClose, drawerOpen }) => (
  <React.Fragment>
    <Reboot />
    <div className={classes.appFrame}>
      <AppToolbar mobile={mobile} drawerOpen={drawerOpen} onDrawerOpen={onDrawerOpen}>
        <Typography variant="title" color="inherit">{Response.title}</Typography>
      </AppToolbar>
      <AppDrawer mobile={mobile} open={drawerOpen} onClose={onDrawerClose}>
        <AppNav />
      </AppDrawer>
      <AppContent mobile={mobile} drawerOpen={drawerOpen}>
        <Body router={router} response={rest} />
      </AppContent>
    </div>
  </React.Fragment>
);

export default enhance(App);