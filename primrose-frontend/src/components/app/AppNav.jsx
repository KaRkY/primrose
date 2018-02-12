import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import List from "material-ui/List";
import NavItem from "../nav/NavItem";

const propTypes = {

};

const styles = theme => ({

});

const enhance = compose(
  withStyles(styles, { withTheme: true })
);

function AppNav() {
  return (
    <List component="nav">
      <NavItem to="Home" text="Home" />
      <NavItem to="Customers" text="Customers" partial />
    </List>
  );
};

const ComposedAppNav = enhance(AppNav);
ComposedAppNav.propTypes = propTypes;

export default ComposedAppNav;