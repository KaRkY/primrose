import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";

import App from "./components/ChildConfigApp";
import List from "material-ui/List";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import NavItem from "./components/nav/NavItem"
import Switcher from "./components/Switcher";

const mapState = ({ title }) => ({
  title,
});

const enhance = compose(
  connect(mapState),
);

const Main = ({
  title
}) => (
  <App>
    <App.Toolbar position="fixed" title={title}>
      <Tooltip title="Actions" enterDelay={300}>
        <IconButton
          variant="raised"
          color="inherit">
          <MoreVert />
        </IconButton>
      </Tooltip>
    </App.Toolbar>

    <App.Navigation>
      <List>
        <NavItem exact to={{ type: "HOME" }} text="Home" />
        <NavItem to={{ type: "CUSTOMERS" }} text="Customers" />
      </List>
    </App.Navigation>

    <App.Content>
      <Switcher />
    </App.Content>
  </App>
);

const EnhancedMain = enhance(Main);

export default EnhancedMain;