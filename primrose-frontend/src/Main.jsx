import React from "react";

import App from "./components/app/App";
import NavItem from "./components/nav/NavItem";
import List from "material-ui/List";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";

const Main = ({ response, router }) => (
  <App>
    <App.Toolbar position="fixed" title={response.title}>
      <Tooltip title="Actions" enterDelay={300}>
        <IconButton
          variant="raised"
          color="inherit">
          <MoreVert />
        </IconButton>
      </Tooltip>
    </App.Toolbar>

    <App.Navigation>
      <List component="nav">
        <NavItem to="Home" text="Home" />
        <NavItem to="Customers" text="Customers" partial />
      </List>
    </App.Navigation>

    <App.Content>
      {React.createElement(response.body, { router, ...response.data })}
    </App.Content>
  </App>
);

export default Main;