import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import classnames from "classnames";
import { withStyles } from "material-ui/styles";

import { ListItem, ListItemText } from "material-ui/List";
import { Link } from "@curi/react";

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
  withStyles(styles)
);

const merge = classes => props => {
  props.className = classnames(props.className, classes.activeMenuItem);
  return props;
};

function NavItem({ classes, text, to, params, details, partial, ...rest }) {
  return (
    <ListItem
      component={(props) => (
        <Link to={to} params={params} details={details} active={{ partial, merge: merge(classes) }} {...props} />
      )}
      button>
      <ListItemText classes={{ primary: classes.activeMenuItemText }} primary={text} />
    </ListItem>
  );
}

const ComposedNavItem = enhance(NavItem);
ComposedNavItem.propTypes = propTypes;

export default ComposedNavItem;