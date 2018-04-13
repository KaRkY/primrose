import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import List, { ListItem, ListItemText, ListItemSecondaryAction } from 'material-ui/List';
import { NavLink } from "redux-first-router-link";
import { CircularProgress } from "material-ui/Progress";
import Collapse from "material-ui/transitions/Collapse";
import ExpandLessIcon from '@material-ui/icons/ExpandLess';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

export const style = theme => ({
  activeMenuItem: {
    backgroundColor: theme.palette.primary.main,
    "& $activeMenuItemText, & $activeMenuItemIcon, & + * $activeMenuItemLoading": {
      color: theme.palette.primary.contrastText,
    },
    "&:hover": {
      backgroundColor: theme.palette.primary.light,
    },
  },
  activeMenuItemText: {},
  activeMenuItemIcon: {},
  activeMenuItemLoading: {
    color: theme.palette.text.primary,
  },
  nested: {
    paddingLeft: theme.spacing.unit * 4,
  },
});

const enhance = compose(
  withStyles(style),
);

const NavItem = ({ classes, name, to, loading, nested, ...rest }) => (
  <ListItem
    component={(props) => (
      <NavLink to={to} activeClassName={classes.activeMenuItem} {...props} {...rest} />
    )}
    button={true}
    className={nested && classes.nested}>
    <ListItemText classes={{ primary: classes.activeMenuItemText }} primary={name} />
    {loading && <ListItemSecondaryAction>
      <CircularProgress
        className={classes.activeMenuItemLoading}
        size={28}
        thickness={4} />
    </ListItemSecondaryAction>}
  </ListItem>
);

class NavItemPanel extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      open: false,
    };
  }

  toggleOpen = () => {
    this.setState(state => ({ open: !state.open }));
  };

  render() {
    const { classes, name, items } = this.props;
    return (
      <React.Fragment>
        <ListItem button={true} onClick={this.toggleOpen}>
          <ListItemText classes={{ primary: classes.activeMenuItemText }} primary={name} />
          {this.state.open ? <ExpandLessIcon /> : <ExpandMoreIcon />}
        </ListItem>
        <Collapse in={this.state.open} timeout="auto" unmountOnExit>
          <List component="div" disablePadding>
            {items.map(item => (
              <NavItem key={item.name} classes={classes} nested {...item} />
            ))}
          </List>
        </Collapse>
      </React.Fragment>
    );
  }
}

const Nav = ({ classes, items = [] }) => (
  <List component="nav">
    {items.map(({ children, ...item }) => children ?
      <NavItemPanel key={item.name} items={children} classes={classes} {...item} /> :
      <NavItem key={item.name} classes={classes} {...item} />)}
  </List>
);

const EnhancedNav = enhance(Nav);

EnhancedNav.propTypes = {
  items: PropTypes.arrayOf(
    PropTypes.oneOfType([
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        to: PropTypes.shape({
          type: PropTypes.string.isRequired
        }).isRequired,
      }).isRequired,
      PropTypes.shape({
        name: PropTypes.string.isRequired,
    
        children: PropTypes.arrayOf(PropTypes.shape({
          name: PropTypes.string.isRequired,
          to: PropTypes.shape({
            type: PropTypes.string.isRequired
          }).isRequired,
        }).isRequired)
      }).isRequired
    ]).isRequired
  ).isRequired,
};

export default EnhancedNav;