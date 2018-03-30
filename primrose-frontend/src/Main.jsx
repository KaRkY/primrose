import React from "react";

import { DApp } from "./components/App";
import List from "material-ui/List";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import NavItem from "./components/nav/NavItem"
import Switcher from "./components/Switcher";

const Main = () => (
  <DApp>
    <DApp.Toolbar position="fixed" title={"test"}>
      <Tooltip title="Actions" enterDelay={300}>
        <IconButton
          variant="raised"
          color="inherit">
          <MoreVert />
        </IconButton>
      </Tooltip>
    </DApp.Toolbar>

    <DApp.Navigation>
      <List>
        <NavItem to={{ type: "HOME" }} text="Home" />
        <NavItem to={{ type: "CUSTOMERS" }} text="Customers" />
      </List>
    </DApp.Navigation>

    <DApp.Content>
      <Switcher />
    </DApp.Content>
  </DApp>
);

export default Main;