import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import { ListItem, ListItemText } from "material-ui/List";
import { NavLink } from "redux-first-router-link";

const propTypes = {
  text: PropTypes.string.isRequired,
};

const styles = theme => ({
  activeMenuItem: {
    backgroundColor: theme.palette.primary.main,
    "& $activeMenuItemText, & $activeMenuItemIcon": {
      color: theme.palette.primary.contrastText,
    },
    "&:hover": {
      backgroundColor: theme.palette.primary.light,
    },
  },
  activeMenuItemText: {},
  activeMenuItemIcon: {},
});

const enhance = compose(
  withStyles(styles),
);

function NavItem({ classes, text, to, ...rest }) {
  return (
    <ListItem
      component={(props) => (
        <NavLink to={to} activeClassName={classes.activeMenuItem} {...props} {...rest} />
      )}
      button>
      <ListItemText classes={{ primary: classes.activeMenuItemText }} primary={text} />
    </ListItem>
  );
}

const ComposedNavItem = enhance(NavItem);
ComposedNavItem.propTypes = propTypes;

export default ComposedNavItem;