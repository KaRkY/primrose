import React from "react";
import PropTypes from "prop-types";

import List from "@material-ui/core/List";
import Divider from "@material-ui/core/Divider";

import NavItem from "../NavItem";
import NavItemPanel from "../NavItemPanel";

const propTypes = {
  classes: PropTypes.shape({
    activeMenuItem: PropTypes.string.isRequired,
    activeMenuItemText: PropTypes.string.isRequired,
    activeMenuItemIcon: PropTypes.string.isRequired,
    activeMenuItemLoading: PropTypes.string.isRequired,
    nested: PropTypes.string.isRequired,
  }).isRequired,
  items: PropTypes.arrayOf(
    PropTypes.oneOfType([
      PropTypes.shape({
        separator: PropTypes.bool.isRequired,
      }).isRequired,
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        to: PropTypes.shape({
          type: PropTypes.string.isRequired
        }).isRequired,
      }).isRequired,
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        open: PropTypes.bool,

        items: PropTypes.arrayOf(PropTypes.shape({
          name: PropTypes.string.isRequired,
          to: PropTypes.shape({
            type: PropTypes.string.isRequired
          }).isRequired,
        }).isRequired)
      }).isRequired
    ]).isRequired
  ).isRequired,
};

const Nav = ({
  classes,
  items = []
}) => {
  const hasIcon = items.reduce((acc, item) => item.icon ? true : acc, false);
  return (
    <List component="nav">
      {
        items.map((item, index) => {
          if(item.separator) {
            return <Divider key={index} inset={hasIcon} />;
          } else {
            if(item.items) {
              return <NavItemPanel key={item.name} inset={hasIcon} classes={classes} {...item} />
            } else {
              return <NavItem key={item.name} inset={hasIcon} classes={classes} {...item} />;
            }
          }
        })
      }
    </List>
  );
};

Nav.propTypes = propTypes;

export default Nav;