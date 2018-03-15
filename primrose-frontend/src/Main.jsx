import React from "react";

import App from "./components/App";
import List from "material-ui/List";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import NavItem from "./components/nav/NavItem"
import { BrowserRouter } from "react-router-dom";
import { Switch, Route } from "react-router";

const Main = () => (
  <BrowserRouter>
    <App
      toolbar={{
        title: (
          <Switch>
            <Route exact path="/" render={() => "Home"} />
            <Route path="/customers" render={() => "Customers"} />
            <Route path="/customers/:id" render={() => "Customer"} />
          </Switch>
        ),
        actions: (
          <Tooltip title="Actions" enterDelay={300}>
            <IconButton
              variant="raised"
              color="inherit">
              <MoreVert />
            </IconButton>
          </Tooltip>
        ),
      }}

      navigation={
      <List>
        <NavItem exact to="/" text="Home" />
        <NavItem to="/customers" text="Customers" />
      </List>
    }

      content={
        <Switch>
          <Route exact path="/" render={() => "Home"} />
          <Route path="/customers" render={() => "Customers"} />
          <Route path="/customers/:id" render={() => "Customer"} />
        </Switch>
      }
    />
  </BrowserRouter>
);

export default Main;