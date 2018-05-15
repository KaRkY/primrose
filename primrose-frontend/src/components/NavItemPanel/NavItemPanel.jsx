import React from "react";
import PropTypes from "prop-types";

import Collapse from "@material-ui/core/Collapse";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";

import ExpandLessIcon from "@material-ui/icons/ExpandLess";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

import NavItem from "../NavItem";

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
  panelOpen: PropTypes.bool.isRequired,
  items: PropTypes.arrayOf(
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
  ).isRequired,
  inset: PropTypes.bool,
  onToggleOpen: PropTypes.func.isRequired,
};

const renderIcon = icon => <ListItemIcon>{icon}</ListItemIcon>;

const NavItemPanel = ({
  classes,
  name,
  icon,
  panelOpen,
  items,
  inset,
  onToggleOpen
}) => (
    <React.Fragment>
      <ListItem button={true} onClick={onToggleOpen}>
        {icon && renderIcon(icon)}
        <ListItemText inset={inset} classes={{ primary: classes.activeMenuItemText }} primary={name} />
        {panelOpen ? <ExpandLessIcon /> : <ExpandMoreIcon />}
      </ListItem>
      <Collapse in={panelOpen} timeout="auto" unmountOnExit>
        <List component="div" disablePadding>
          {items.map(item => (
            <NavItem inset={inset} key={item.name} classes={classes} nested {...item} />
          ))}
        </List>
      </Collapse>
    </React.Fragment>
  );

NavItemPanel.propTypes = propTypes;
export default NavItemPanel;