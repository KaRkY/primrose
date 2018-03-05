import React from "react";
import { withStyles } from "material-ui/styles";
import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import withWidth, { isWidthDown } from "material-ui/utils/withWidth";
import mapProps from "recompose/mapProps";
import findByType from "../util/findByType";
import get from "lodash/get";
import classNames from "classnames";

import Reboot from "material-ui/Reboot";
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
    height: "100vh",
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
    height: "calc(100% - 56px)",
    marginTop: 56,
    [theme.breakpoints.up("sm")]: {
      content: {
        height: "calc(100% - 64px)",
        marginTop: 64,
      },
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
      margin: "0 auto",
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

const Toolbar = () => null;
Toolbar.displayName = "Toolbar";

const Navigation = () => null;
Navigation.displayName = "Navigation";

const Content = () => null;
Content.displayName = "Content";

const App = ({ classes, theme, mobile, onDrawerOpen, onDrawerClose, drawerOpen, children }) => {
  const toolbarComponent = findByType(children, Toolbar)[0];
  const navigationComponent = findByType(children, Navigation)[0];
  const contentComponent = findByType(children, Content)[0];

  const isToolbar = toolbarComponent ? true : false;
  const title = get(toolbarComponent, "props.title");
  const position = get(toolbarComponent, "props.position");
  const toolbarActions = get(toolbarComponent, "props.children", null);

  const isNavigation = navigationComponent ? true : false;
  const navigation = get(navigationComponent, "props.children", null);

  const isContent = contentComponent ? true : false;
  const content = get(contentComponent, "props.children", null);

  const anchor = theme.direction === "rtl" ? "right" : "left";

  const toolbarClasses = classNames(classes["app-bar"], {
    [classes["app-bar-shift"]]: isNavigation && drawerOpen && !mobile,
    [classes[`app-bar-shift-${anchor}`]]: isNavigation && drawerOpen && !mobile,
  });

  const contentClasses = classNames(classes["app-content"], classes[`app-content-${anchor}`], {
    [classes["app-content-shift"]]: isNavigation && drawerOpen && !mobile,
    [classes[`app-content-shift-${anchor}`]]: isNavigation && drawerOpen && !mobile,
  });

  return (
    <React.Fragment>
      <Reboot />
      <div className={classes.root}>
        {isToolbar && (
          <AppBar
            position={position}
            className={toolbarClasses}>
            <AppToolbar>

              {isNavigation && !drawerOpen && <IconButton
                color="default"
                aria-label="open drawer"
                onClick={onDrawerOpen}
              >
                <MenuIcon />
              </IconButton>}

              <Typography variant="title" color="inherit">{title}</Typography>
              <div className={classes.grow} />
              {toolbarActions}
            </AppToolbar>
          </AppBar>
        )}

        {isNavigation && (
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

        {isContent && (
          <main className={contentClasses}>
            <div className={classes["app-center"]}>
              {content}
            </div>
          </main>
        )}
      </div>
    </React.Fragment>
  );
};

const EnhancedApp = enhance(App);
EnhancedApp.Toolbar = Toolbar;
EnhancedApp.Navigation = Navigation;
EnhancedApp.Content = Content;

export default EnhancedApp;