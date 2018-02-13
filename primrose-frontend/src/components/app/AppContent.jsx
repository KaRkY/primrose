import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import classNames from "classnames";
import { withStyles } from "material-ui/styles";

const propTypes = {
  drawerOpen: PropTypes.bool.isRequired,
  children: PropTypes.node,
  mobile: PropTypes.bool.isRequired,
};

const styles = theme => ({
  content: {
    width: "100%",
    flexGrow: 1,
    backgroundColor: theme.palette.background.default,
    padding: theme.spacing.unit * 3,
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    height: "calc(100% - 56px)",
    marginTop: 56,
    [theme.breakpoints.up("sm")]: {
      content: {
        height: "calc(100% - 64px)",
        marginTop: 64,
      },
    },
  },
  "content-left": {
    marginLeft: 0,
  },
  "content-right": {
    marginRight: 0,
  },
  contentShift: {
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  "contentShift-left": {
    marginLeft: theme.drawer.width,
  },
  "contentShift-right": {
    marginRight: theme.drawer.width,
  },
  center: {
    [theme.breakpoints.up("sm")]: {
      //"max-width": 1170,
      margin: "0 auto",
    },
  },
});

const enhance = compose(
  withStyles(styles, { withTheme: true })
);

function AppContent({ classes, theme, drawerOpen, children, mobile }) {
  const anchor = theme.direction === "rtl" ? "right" : "left";
  return (
    <main 
      className={classNames(classes.content, classes[`content-${anchor}`], {
        [classes.contentShift]: drawerOpen && !mobile,
        [classes[`contentShift-${anchor}`]]: drawerOpen && !mobile,
      })}>
        <div className={classes.center}>
          {children}
        </div>
      </main>
  );
};

const ComposedAppContent = enhance(AppContent);
ComposedAppContent.propTypes = propTypes;

export default ComposedAppContent;