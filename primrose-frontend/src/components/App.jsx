import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "material-ui/styles";
import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import withWidth, { isWidthDown } from "material-ui/utils/withWidth";
import mapProps from "recompose/mapProps";
import classNames from "classnames";
import dynamic from "./dynamic";

import CssBaseline from "material-ui/CssBaseline";
import Typography from "material-ui/Typography";
import Drawer from "material-ui/Drawer";
import Divider from "material-ui/Divider";
import IconButton from "material-ui/IconButton";
import ChevronLeftIcon from "material-ui-icons/ChevronLeft";
import ChevronRightIcon from "material-ui-icons/ChevronRight";
import AppBar from "material-ui/AppBar";
import AppToolbar from "material-ui/Toolbar";
import MenuIcon from "material-ui-icons/Menu";

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
      onDrawerOpen: ({ updateDrawerOpen }) => () => ({ drawerOpen: true }),
      onDrawerClose: ({ updateDrawerOpen }) => () => ({ drawerOpen: false }),
    }
  ),
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
    content
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

  return (
    <React.Fragment>
      <CssBaseline />
      <div className={classes.root}>
        {toolbar && (
          <AppBar
            position={toolbar.position}
            className={toolbarClasses}>
            <AppToolbar>

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
            </AppToolbar>
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
              {navigation.children}
            </div>
          </Drawer>
        )}

        {content && (
          <main className={contentClasses}>
            <div className={classes["app-content-center"]}>
              {content.children}
            </div>
          </main>
        )}
      </div>
    </React.Fragment>
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
  navigation: PropTypes.shape({
    children: PropTypes.element
  }),
  content: PropTypes.shape({
    children: PropTypes.element
  }),
};

export const DApp = dynamic({
  Toolbar: {
    key: "toolbar",
    propTypes: {
      title: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.element,
      ]).isRequired,
      position: PropTypes.oneOf(["static", "fixed"]),
    },
    renderChildren: true,
    mapProps: {
      children: "actions"
    }
  },

  Navigation: {
    key: "navigation",
    renderChildren: true,
  },

  Content: {
    key: "content",
    renderChildren: true,
  },
})(EnhancedApp);

export default EnhancedApp;