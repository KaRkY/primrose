import React from "react";
import PropTypes from "prop-types";

import { NavLink } from "redux-first-router-link";

import CircularProgress from "@material-ui/core/CircularProgress";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import ListItemText from "@material-ui/core/ListItemText";

const propTypes = {
  classes: PropTypes.shape({
    activeMenuItem: PropTypes.string.isRequired,
    activeMenuItemText: PropTypes.string.isRequired,
    activeMenuItemIcon: PropTypes.string.isRequired,
    activeMenuItemLoading: PropTypes.string.isRequired,
    nested: PropTypes.string.isRequired,
  }).isRequired,
  name: PropTypes.string.isRequired,
  icon: PropTypes.element,
  inset: PropTypes.bool,
  to: PropTypes.object.isRequired,
  loading: PropTypes.bool,
  nested: PropTypes.bool,
};

const renderIcon = icon => <ListItemIcon>{icon}</ListItemIcon>;
const renderLoading = loadingClass =>
  <ListItemSecondaryAction>
    <CircularProgress
      className={loadingClass}
      size={28}
      thickness={4} />
  </ListItemSecondaryAction>;

const NavItem = ({
  classes,
  name,
  icon,
  inset,
  to,
  loading,
  nested,
  ...rest
}) => (
    <ListItem
      component={(props) => (
        <NavLink to={to} activeClassName={classes.activeMenuItem} {...props} {...rest} />
      )}
      button={true}
      className={nested && classes.nested}>
      {icon && renderIcon(icon)}
      <ListItemText inset={inset} classes={{ primary: classes.activeMenuItemText }} primary={name} />
      {loading && renderLoading(classes.activeMenuItemLoading)}
    </ListItem>
  );
NavItem.propTypes = propTypes;
export default NavItem;