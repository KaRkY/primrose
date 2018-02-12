import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import classNames from "classnames";
import { withStyles } from "material-ui/styles";

import AppBar from "material-ui/AppBar";
import Toolbar from "material-ui/Toolbar";
import IconButton from "material-ui/IconButton";
import MenuIcon from "material-ui-icons/Menu";

const propTypes = {
  drawerOpen: PropTypes.bool.isRequired,
  onDrawerOpen: PropTypes.func.isRequired,
  mobile: PropTypes.bool.isRequired,
};

const styles = theme => ({
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
});

const enhance = compose(
  withStyles(styles, { withTheme: true })
);

function AppToolbar({ classes, theme, drawerOpen, onDrawerOpen, mobile, children }) {
  const anchor = theme.direction === "rtl" ? "right" : "left";
  return (
    <AppBar
      className={classNames(classes["app-bar"], {
        [classes["app-bar-shift"]]: drawerOpen && !mobile,
        [classes[`app-bar-shift-${anchor}`]]: drawerOpen && !mobile,
      })}>
      <Toolbar>
        {!drawerOpen && <IconButton
          color="default"
          aria-label="open drawer"
          onClick={onDrawerOpen}
        >
          <MenuIcon />
        </IconButton>
        }
        {children}
      </Toolbar>
    </AppBar>
  );
};

const ComposedAppToolbar = enhance(AppToolbar);
ComposedAppToolbar.propTypes = propTypes;

export default ComposedAppToolbar;