import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import {
  NotificationContext
} from "../../contexts";

import compose from "recompose/compose";
import mapProps from "recompose/mapProps";
import withHandlers from "recompose/withHandlers";
import withStateHandlers from "recompose/withStateHandlers";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import IconButton from "@material-ui/core/IconButton";
import Snackbar from "@material-ui/core/Snackbar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import withWidth, { isWidthDown } from "@material-ui/core/withWidth";

import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import ChevronRightIcon from "@material-ui/icons/ChevronRight";
import MenuIcon from "@material-ui/icons/Menu";
import CloseIcon from "@material-ui/icons/Close";

const styles = theme => ({
  root: {
    position: "relative",
    display: "flex",
    width: "100%",
    height: "100vh",
  },

  "app-bar": {
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  "app-bar-shift": {
    width: `calc(100% - ${theme.drawer.width}px)`,
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  "app-bar-shift-left": {
    marginLeft: theme.drawer.width,
  },
  "app-bar-shift-right": {
    marginRight: theme.drawer.width,
  },

  "app-drawer-header": {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: "0 8px",
    ...theme.mixins.toolbar
  },

  "app-drawer-paper": {
    width: theme.drawer.width,
  },

  "app-content": {
    width: "100%",
    flexGrow: 1,
    backgroundColor: theme.palette.background.default,
    padding: theme.spacing.unit * 3,
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    minHeight: "calc(100vh - 56px)",
    marginTop: 56,
    [theme.breakpoints.up("sm")]: {
      minHeight: "calc(100vh - 64px)",
      marginTop: 64,
    },
  },

  "app-content-left": {
    marginLeft: 0,
  },
  "app-content-right": {
    marginRight: 0,
  },
  "app-content-shift": {
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  "app-content-shift-left": {
    marginLeft: theme.drawer.width,
  },
  "app-content-shift-right": {
    marginRight: theme.drawer.width,
  },
  "app-content-center": {
    [theme.breakpoints.up("sm")]: {
      //"max-width": 1170,
      position: "relative",
      margin: "0 auto",
      minHeight: "100%",
    },
  },

  grow: {
    flex: "1 1 auto",
  },
});

const enhance = compose(
  withWidth(),
  mapProps(({ width, ...rest }) => ({ mobile: isWidthDown("md", width), ...rest })),
  withStateHandlers(
    ({ mobile }) => ({ drawerOpen: !mobile }),
    {
      onDrawerOpen: ({ drawerOpen, ...props }) => () => ({ drawerOpen: true, ...props }),
      onDrawerClose: ({ drawerOpen, ...props }) => () => ({ drawerOpen: false, ...props }),
    }
  ),
  withStateHandlers(
    () => ({ open: false, current: {}, key: null, queue: [] }),
    {
      push: ({ queue, ...props }) => item => ({ ...props, queue: [...queue, { ...item, key: new Date().getTime() }] }),
      process: ({ queue, ...props }) => () => {
        if (queue.length > 0) {
          const [first, ...rest] = queue;
          return ({ ...props, open: true, current: first, queue: rest });
        } else {
          return ({ ...props, open: false, current: {}, queue });
        }
      },
      close: ({ open, ...props }) => (event, reason) => ({ ...props, open: reason === "clickaway" ? open : false }),
    }),
  withHandlers({
    push: ({ push, open, process }) => item => {
      push(item);
      if (!open) {
        process();
      }
    },
  }),
  withStyles(styles, { withTheme: true }),
);

const App = (props) => {

  const {
    classes,
    theme,
    mobile,
    drawerOpen,
    onDrawerOpen,
    onDrawerClose,
    toolbar,
    navigation,
    content,
    push,
    close,
    open,
    current,
    process
  } = props;

  const anchor = theme.direction === "rtl" ? "right" : "left";

  const toolbarClasses = classNames(classes["app-bar"], {
    [classes["app-bar-shift"]]: !!navigation && drawerOpen && !mobile,
    [classes[`app-bar-shift-${anchor}`]]: !!navigation && drawerOpen && !mobile,
  });

  const contentClasses = classNames(classes["app-content"], classes[`app-content-${anchor}`], {
    [classes["app-content-shift"]]: !!navigation && drawerOpen && !mobile,
    [classes[`app-content-shift-${anchor}`]]: !!navigation && drawerOpen && !mobile,
  });

  const snackbarActions = [];
  if (current.undo) {
    snackbarActions.push(<Button
      key="undo"
      color="secondary"
      size="small"
      onClick={current.undo}>UNDO</Button>);
  }
  snackbarActions.push(<IconButton
    key="close"
    aria-label="Close"
    color="inherit"
    className={classes.close}
    onClick={close}
  >
    <CloseIcon />
  </IconButton>);

  return (
    <NotificationContext.Provider
      value={{
        push,
        close,
        open,
        message: current.message,
        key: current.key,
        exit: process,
      }}
    >
      <CssBaseline />
      <div className={classes.root}>
        {toolbar && (
          <AppBar
            position={toolbar.position}
            className={toolbarClasses}>
            <Toolbar>

              {navigation && !drawerOpen && <IconButton
                color="default"
                aria-label="open drawer"
                onClick={onDrawerOpen}
              >
                <MenuIcon />
              </IconButton>}

              <Typography variant="title" color="inherit">{toolbar.title}</Typography>
              <div className={classes.grow} />
              {toolbar.actions}
            </Toolbar>
          </AppBar>
        )}

        {navigation && (
          <Drawer
            variant={mobile ? "temporary" : "persistent"}
            anchor={anchor}
            open={drawerOpen}
            classes={{
              paper: classes["app-drawer-paper"],
            }}
            onClose={onDrawerClose}
            ModalProps={{
              keepMounted: mobile, // Better open performance on mobile.
            }}
          >
            <div>
              <div className={classes["app-drawer-header"]}>
                <IconButton onClick={onDrawerClose}>
                  {theme.direction === "rtl" ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                </IconButton>
              </div>
              <Divider />
              {navigation}
            </div>
          </Drawer>
        )}

        {content && (
          <main className={contentClasses}>
            <div className={classes["app-content-center"]}>
              {content}
            </div>
          </main>
        )}
      </div>

      <Snackbar
        key={current.key}
        anchorOrigin={{
          vertical: "top",
          horizontal: "right",
        }}
        open={open}
        autoHideDuration={4000}
        onClose={close}
        onExited={process}
        ContentProps={{
          "aria-describedby": "message-id",
        }}
        message={<span id="message-id">{current.text}</span>}
        action={snackbarActions}
      />
    </NotificationContext.Provider>
  );
};

const EnhancedApp = enhance(App);
EnhancedApp.propTypes = {
  toolbar: PropTypes.shape({
    title: PropTypes.oneOfType([
      PropTypes.string,
      PropTypes.element,
    ]).isRequired,
    position: PropTypes.oneOf(["static", "fixed"]),
    actions: PropTypes.element,
  }),
  navigation: PropTypes.element,
  content: PropTypes.element,
};

export default EnhancedApp;